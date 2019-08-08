package com.anvipo.angram.applicationLayer.launchSystem.appActivity.presenter

import android.util.Log
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.R
import com.anvipo.angram.applicationLayer.coordinator.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.launchSystem.App
import com.anvipo.angram.applicationLayer.launchSystem.appActivity.view.AppView
import com.anvipo.angram.applicationLayer.types.ConnectionState.*
import com.anvipo.angram.applicationLayer.types.ConnectionStateReceiveChannel
import com.anvipo.angram.applicationLayer.types.SystemMessageReceiveChannel
import com.anvipo.angram.coreLayer.CoreHelpers.debugLog
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.coreLayer.message.SystemMessageType
import com.anvipo.angram.presentationLayer.common.baseClasses.BasePresenterImp
import com.arellomobile.mvp.InjectViewState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@InjectViewState
class AppPresenterImp(
    private val coordinator: ApplicationCoordinator,
    private val systemMessageReceiveChannel: SystemMessageReceiveChannel,
    private val connectionStateReceiveChannel: ConnectionStateReceiveChannel,
    private val resourceManager: ResourceManager
) : BasePresenterImp<AppView>(), AppPresenter {

    override fun coldStart() {
        coordinator.coldStart()
    }

    override fun hotStart() {
        coordinator.hotStart()
    }

    override fun onResumeFragments() {
        subscribeToChannels()
        viewState.setNavigator()
    }

    override fun onPauseTriggered() {
        viewState.removeNavigator()
        unsubscribeFromChannels()
    }


    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override fun cancelAllJobs() {
        receiveSystemMessagesJob?.cancel()
        receiveConnectionStatesJob?.cancel()
        showToastJob?.cancel()
        showAlertJob?.cancel()
        showSnackbarJob?.cancel()
    }


    private fun subscribeOnSystemMessages() {
        val receiveSystemMessagesCoroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
            if (BuildConfig.DEBUG) {
                val text = error.localizedMessage
                debugLog(text)
                viewState.showErrorAlert(text)
            }
        }

        receiveSystemMessagesJob = launch(
            context = coroutineContext + receiveSystemMessagesCoroutineExceptionHandler
        ) {
            val (text, type, shouldBeShownToUser, shouldBeShownInLogs) = systemMessageReceiveChannel.receive()

            if (shouldBeShownToUser) {
                when (type) {
                    SystemMessageType.TOAST -> {
                        val showToastCoroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
                            if (BuildConfig.DEBUG) {
                                val errorText = error.localizedMessage
                                debugLog(errorText)
                                viewState.showErrorAlert(errorText)
                            }
                        }

                        showToastJob = launch(
                            context = Dispatchers.Main + showToastCoroutineExceptionHandler
                        ) {
                            viewState.showToastMessage(text)
                        }
                    }
                    SystemMessageType.ALERT -> {
                        val showAlertCoroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
                            if (BuildConfig.DEBUG) {
                                val errorText = error.localizedMessage
                                debugLog(errorText)
                                viewState.showErrorAlert(errorText)
                            }
                        }

                        showAlertJob = launch(
                            context = Dispatchers.Main + showAlertCoroutineExceptionHandler
                        ) {
                            viewState.showAlertMessage(text)
                        }
                    }
                }
            }

            if (shouldBeShownInLogs) {
                Log.d(App.TAG, text)
            }
        }
    }

    private fun subscribeOnConnectionStates() {
        val receiveConnectionStatesCoroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
            if (BuildConfig.DEBUG) {
                val text = error.localizedMessage
                debugLog(text)
                viewState.showErrorAlert(text)
            }
        }

        receiveConnectionStatesJob = launch(
            context = coroutineContext + receiveConnectionStatesCoroutineExceptionHandler
        ) {
            val receivedConnectionState = connectionStateReceiveChannel.receive()

            if (receivedConnectionState == Undefined) {
                val errorText = "receivedConnectionState == Undefined"
                debugLog(errorText)
                if (BuildConfig.DEBUG) {
                    viewState.showToastMessage(errorText)
                }

                return@launch
            }

            val connectionState = resourceManager.getString(R.string.connection_state)
            val text: String
            val duration: Int
            when (receivedConnectionState) {
                WaitingForNetwork -> {
                    text = "$connectionState: waiting for network"
                    duration = Snackbar.LENGTH_INDEFINITE
                }
                ConnectingToProxy -> {
                    text = "$connectionState: connecting to proxy"
                    duration = Snackbar.LENGTH_INDEFINITE
                }
                Connecting -> {
                    text = "$connectionState: connecting"
                    duration = Snackbar.LENGTH_INDEFINITE
                }
                Updating -> {
                    text = "$connectionState: updating"
                    duration = Snackbar.LENGTH_INDEFINITE
                }
                Ready -> {
                    text = "$connectionState: connected"
                    duration = Snackbar.LENGTH_LONG
                }
                Undefined -> {
                    text = "receivedConnectionState == Undefined"
                    duration = Snackbar.LENGTH_LONG
                }
            }

            val showSnackbarCEH = CoroutineExceptionHandler { _, error ->
                if (BuildConfig.DEBUG) {
                    val errorText = error.localizedMessage
                    debugLog(errorText)
                    viewState.showErrorAlert(errorText)
                }
            }

            showSnackbarJob = launch(
                context = Dispatchers.Main + showSnackbarCEH
            ) {
                viewState.showConnectionStateSnackMessage(
                    text = text,
                    duration = duration
                )
            }
        }
    }

    private fun unsubscribeFromChannels() {
        systemMessageReceiveChannel.cancel(CancellationException("AppActivity onPause"))
        connectionStateReceiveChannel.cancel(CancellationException("AppActivity onPause"))
    }

    private fun subscribeToChannels() {
        subscribeOnSystemMessages()
        subscribeOnConnectionStates()
    }

    private var showSnackbarJob: Job? = null
    private var showToastJob: Job? = null
    private var showAlertJob: Job? = null
    private var receiveSystemMessagesJob: Job? = null
    private var receiveConnectionStatesJob: Job? = null

}