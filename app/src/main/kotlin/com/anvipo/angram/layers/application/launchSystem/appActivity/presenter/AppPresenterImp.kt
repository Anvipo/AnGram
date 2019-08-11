package com.anvipo.angram.layers.application.launchSystem.appActivity.presenter

import com.anvipo.angram.R
import com.anvipo.angram.layers.application.coordinator.ApplicationCoordinator
import com.anvipo.angram.layers.application.launchSystem.appActivity.view.AppView
import com.anvipo.angram.layers.application.types.ConnectionStateReceiveChannel
import com.anvipo.angram.layers.application.types.EnabledProxyIdReceiveChannel
import com.anvipo.angram.layers.application.types.SystemMessageReceiveChannel
import com.anvipo.angram.layers.businessLogic.useCases.app.AppUseCase
import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.core.CoroutineExceptionHandlerWithLogger
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.core.message.SystemMessage
import com.anvipo.angram.layers.core.message.SystemMessageType
import com.anvipo.angram.layers.presentation.common.baseClasses.BasePresenterImp
import com.arellomobile.mvp.InjectViewState
import com.google.android.material.snackbar.Snackbar
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
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name

        val startApplicationFlowCEH =
            CoroutineExceptionHandlerWithLogger { _, error ->
                viewState.showErrorAlert(error.localizedMessage)
            }

        launch(coroutineContext + startApplicationFlowCEH) {
            val applicationFlowCoordinateResult = coordinator.start()

            myLog(
                invokationPlace = invokationPlace,
                currentParameters = "applicationFlowCoordinateResult = $applicationFlowCoordinateResult"
            )
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
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


    private fun subscribeToChannels() {
        subscribeOnSystemMessages()
        subscribeOnConnectionStates()
        subscribeOnEnabledProxyId()
    }

    private fun subscribeOnSystemMessages() {
        val receiveSystemMessagesCEH = CoroutineExceptionHandlerWithLogger { _, error ->
            viewState.showErrorAlert(error.localizedMessage)
        }

        launch(
            context = coroutineContext + receiveSystemMessagesCEH
        ) {
            for (receivedSystemMessage in systemMessageReceiveChannel) {
                onReceivedSystemMessage(receivedSystemMessage)
            }
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    private fun subscribeOnConnectionStates() {
        val receiveConnectionStatesCEH = CoroutineExceptionHandlerWithLogger { _, error ->
            viewState.showErrorAlert(error.localizedMessage)
        }

        launch(
            context = coroutineContext + receiveConnectionStatesCEH
        ) {
            for (receivedConnectionState in connectionStateReceiveChannel) {
                onReceivedConnectionState(receivedConnectionState)
            }
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    private fun subscribeOnEnabledProxyId() {
        val receiveEnabledProxyIdCEH = CoroutineExceptionHandlerWithLogger { _, error ->
            viewState.showErrorAlert(error.localizedMessage)
        }

        launch(
            context = coroutineContext + receiveEnabledProxyIdCEH
        ) {
            for (receivedEnabledProxyId in enabledProxyIdReceiveChannel) {
                onReceivedEnabledProxyId(receivedEnabledProxyId)
            }
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    private fun onReceivedSystemMessage(receivedSystemMessage: SystemMessage) {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name
        val (text, type, shouldBeShownToUser, shouldBeShownInLogs) = receivedSystemMessage

        if (shouldBeShownToUser) {
            when (type) {
                SystemMessageType.TOAST -> {
                    val showToastCEH = CoroutineExceptionHandlerWithLogger { _, error ->
                        viewState.showErrorAlert(error.localizedMessage)
                    }

                    launch(
                        context = Dispatchers.Main + showToastCEH
                    ) {
                        viewState.showToastMessage(text)
                    }.also { jobsThatWillBeCancelledInOnDestroy += it }
                }
                SystemMessageType.ALERT -> {
                    val showAlertCEH = CoroutineExceptionHandlerWithLogger { _, error ->
                        viewState.showErrorAlert(error.localizedMessage)
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
            myLog(
                invokationPlace = invokationPlace,
                currentParameters = text
            )
        }
    }

    private fun onReceivedConnectionState(receivedConnectionState: TdApi.ConnectionState) {
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
                duration = Snackbar.LENGTH_SHORT
            }
            else -> {
                assertionFailure("Undefined received connection state = $receivedConnectionState")
                text = "$connectionState: ERROR"
                duration = Snackbar.LENGTH_INDEFINITE
            }
        }

        val showSnackbarCEH = CoroutineExceptionHandlerWithLogger { _, error ->
            viewState.showErrorAlert(error.localizedMessage)
        }

        launch(
            context = Dispatchers.Main + showSnackbarCEH
        ) {
            viewState.showConnectionStateSnackMessage(
                text = text,
                duration = duration
            )
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    private suspend fun onReceivedEnabledProxyId(receivedEnabledProxyId: Int?) {
        useCase.saveEnabledProxyId(receivedEnabledProxyId)
    }

}