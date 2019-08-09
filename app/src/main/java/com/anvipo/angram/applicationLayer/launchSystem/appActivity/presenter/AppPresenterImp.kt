package com.anvipo.angram.applicationLayer.launchSystem.appActivity.presenter

import android.util.Log
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.R
import com.anvipo.angram.applicationLayer.coordinator.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.launchSystem.App
import com.anvipo.angram.applicationLayer.launchSystem.appActivity.view.AppView
import com.anvipo.angram.applicationLayer.types.ConnectionStateReceiveChannel
import com.anvipo.angram.applicationLayer.types.EnabledProxyIdReceiveChannel
import com.anvipo.angram.applicationLayer.types.SystemMessageReceiveChannel
import com.anvipo.angram.businessLogicLayer.useCases.app.AppUseCase
import com.anvipo.angram.coreLayer.CoreHelpers.assertionFailure
import com.anvipo.angram.coreLayer.CoreHelpers.debugLog
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.coreLayer.message.SystemMessageType
import com.anvipo.angram.presentationLayer.common.baseClasses.BasePresenterImp
import com.arellomobile.mvp.InjectViewState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import kotlin.coroutines.CoroutineContext

@InjectViewState
class AppPresenterImp(
    private val useCase: AppUseCase,
    private val coordinator: ApplicationCoordinator,
    private val enabledProxyIdReceiveChannel: EnabledProxyIdReceiveChannel,
    private val systemMessageReceiveChannel: SystemMessageReceiveChannel,
    private val connectionStateReceiveChannel: ConnectionStateReceiveChannel,
    private val resourceManager: ResourceManager
) : BasePresenterImp<AppView>(), AppPresenter {

    init {
        val receiveChannelList = listOf(
            enabledProxyIdReceiveChannel,
            systemMessageReceiveChannel,
            connectionStateReceiveChannel
        )

        channelsThatWillBeUnsubscribedInOnPause.addAll(receiveChannelList)
    }

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
        super<BasePresenterImp>.onPauseTriggered()
        viewState.removeNavigator()
    }


    override val coroutineContext: CoroutineContext = Dispatchers.IO


    private fun subscribeOnSystemMessages() {
        val receiveSystemMessagesCEH = CoroutineExceptionHandler { _, error ->
            if (BuildConfig.DEBUG) {
                val text = error.localizedMessage
                debugLog(text)
                viewState.showErrorAlert(text)
            }
        }

        launch(
            context = coroutineContext + receiveSystemMessagesCEH
        ) {
            val (text, type, shouldBeShownToUser, shouldBeShownInLogs) = systemMessageReceiveChannel.receive()

            if (shouldBeShownToUser) {
                when (type) {
                    SystemMessageType.TOAST -> {
                        val showToastCEH = CoroutineExceptionHandler { _, error ->
                            if (BuildConfig.DEBUG) {
                                val errorText = error.localizedMessage
                                debugLog(errorText)
                                viewState.showErrorAlert(errorText)
                            }
                        }

                        launch(
                            context = Dispatchers.Main + showToastCEH
                        ) {
                            viewState.showToastMessage(text)
                        }.also { jobsThatWillBeCancelledInOnDestroy += it }
                    }
                    SystemMessageType.ALERT -> {
                        val showAlertCEH = CoroutineExceptionHandler { _, error ->
                            if (BuildConfig.DEBUG) {
                                val errorText = error.localizedMessage
                                debugLog(errorText)
                                viewState.showErrorAlert(errorText)
                            }
                        }

                        launch(
                            context = Dispatchers.Main + showAlertCEH
                        ) {
                            viewState.showAlertMessage(text)
                        }.also { jobsThatWillBeCancelledInOnDestroy += it }
                    }
                }
            }

            if (shouldBeShownInLogs) {
                Log.d(App.TAG, text)
            }
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    private fun subscribeOnConnectionStates() {
        val receiveConnectionStatesCEH = CoroutineExceptionHandler { _, error ->
            if (BuildConfig.DEBUG) {
                val text = error.localizedMessage
                debugLog(text)
                viewState.showErrorAlert(text)
            }
        }

        launch(
            context = coroutineContext + receiveConnectionStatesCEH
        ) {
            val receivedConnectionState = connectionStateReceiveChannel.receive()

            val connectionState = resourceManager.getString(R.string.connection_state)
            val text: String
            val duration: Int

            when (receivedConnectionState) {
                is TdApi.ConnectionStateWaitingForNetwork -> {
                    text = "$connectionState: waiting for network"
                    duration = Snackbar.LENGTH_INDEFINITE
                }
                is TdApi.ConnectionStateConnectingToProxy -> {
                    text = "$connectionState: connecting to proxy"
                    duration = Snackbar.LENGTH_INDEFINITE
                }
                is TdApi.ConnectionStateConnecting -> {
                    text = "$connectionState: connecting"
                    duration = Snackbar.LENGTH_INDEFINITE
                }
                is TdApi.ConnectionStateUpdating -> {
                    text = "$connectionState: updating"
                    duration = Snackbar.LENGTH_INDEFINITE
                }
                is TdApi.ConnectionStateReady -> {
                    text = "$connectionState: connected"
                    duration = Snackbar.LENGTH_LONG
                }
                else -> {
                    assertionFailure()
                    text = ""
                    duration = Snackbar.LENGTH_SHORT
                }
            }

            val showSnackbarCEH = CoroutineExceptionHandler { _, error ->
                if (BuildConfig.DEBUG) {
                    val errorText = error.localizedMessage
                    debugLog(errorText)
                    viewState.showErrorAlert(errorText)
                }
            }

            launch(
                context = Dispatchers.Main + showSnackbarCEH
            ) {
                viewState.showConnectionStateSnackMessage(
                    text = text,
                    duration = duration
                )
            }.also { jobsThatWillBeCancelledInOnDestroy += it }
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    private fun subscribeOnEnabledProxyId() {
        val receiveEnabledProxyIdCEH = CoroutineExceptionHandler { _, error ->
            if (BuildConfig.DEBUG) {
                val text = error.localizedMessage
                debugLog(text)
                viewState.showErrorAlert(text)
            }
        }

        launch(
            context = coroutineContext + receiveEnabledProxyIdCEH
        ) {
            val enabledProxyId = enabledProxyIdReceiveChannel.receive()

            useCase.saveEnabledProxyId(enabledProxyId)
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    private fun subscribeToChannels() {
        subscribeOnSystemMessages()
        subscribeOnConnectionStates()
        subscribeOnEnabledProxyId()
    }

}