package com.anvipo.angram.applicationLayer.launchSystem.appActivity.presenter

import android.util.Log
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.R
import com.anvipo.angram.applicationLayer.coordinator.ApplicationCoordinatorOutput
import com.anvipo.angram.applicationLayer.launchSystem.App
import com.anvipo.angram.applicationLayer.launchSystem.appActivity.view.AppView
import com.anvipo.angram.applicationLayer.types.ConnectionState.*
import com.anvipo.angram.applicationLayer.types.ConnectionStateReceiveChannel
import com.anvipo.angram.applicationLayer.types.SystemMessageReceiveChannel
import com.anvipo.angram.businessLogicLayer.useCases.base.BaseUseCase
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
    override val coordinator: ApplicationCoordinatorOutput,
    private val systemMessageReceiveChannel: SystemMessageReceiveChannel,
    private val connectionStateReceiveChannel: ConnectionStateReceiveChannel,
    private val resourceManager: ResourceManager
) : BasePresenterImp<AppView>(), AppPresenter {

    override fun onPause() {
        viewState.removeNavigator()
        unsubscribeFromChannels()
    }

    override fun onResumeFragments() {
        subscribeToChannels()
        viewState.setNavigator()
    }

    override fun coldStart() {
        coordinator.coldStartApp()
    }

    override fun hotStart() {
        coordinator.hotStartApp()
    }

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override fun cancelAllJobs() {
        receiveSystemMessagesJob?.cancel()
        receiveConnectionStatesJob?.cancel()
        showToastJob?.cancel()
        showAlertJob?.cancel()
        showSnackbarJob?.cancel()
    }

    override val useCase: BaseUseCase
        get() = TODO("not implemented")

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
            val text: String
            val duration: Int
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
                    duration = Snackbar.LENGTH_SHORT
                }
            }

            val showSnackbarCoroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
                if (BuildConfig.DEBUG) {
                    val errorText = error.localizedMessage
                    debugLog(errorText)
                    viewState.showErrorAlert(errorText)
                }
            }

            showSnackbarJob = launch(
                context = Dispatchers.Main + showSnackbarCoroutineExceptionHandler
            ) {
                viewState.showSnackMessage(
                    text = text,
                    duration = duration,
                    withProgressBar = duration == Snackbar.LENGTH_INDEFINITE,
                    isProgressBarIndeterminate = duration == Snackbar.LENGTH_INDEFINITE
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