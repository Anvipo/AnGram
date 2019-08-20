package com.anvipo.angram.layers.application.launchSystem.appActivity.viewModel

import com.anvipo.angram.R
import com.anvipo.angram.layers.application.coordinator.ApplicationCoordinator
import com.anvipo.angram.layers.application.launchSystem.appActivity.types.SetNavigatorEventParameters
import com.anvipo.angram.layers.application.launchSystem.appActivity.types.SetNavigatorEventParameters.REMOVE
import com.anvipo.angram.layers.application.launchSystem.appActivity.types.SetNavigatorEventParameters.SET
import com.anvipo.angram.layers.application.launchSystem.appActivity.types.TDLibClientHasBeenRecreatedReceiveChannel
import com.anvipo.angram.layers.businessLogic.useCases.app.AppUseCase
import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.core.ShowSnackMessageEventParameters
import com.anvipo.angram.layers.core.base.classes.BaseViewModelImpl
import com.anvipo.angram.layers.core.events.SingleLiveEvent
import com.anvipo.angram.layers.core.events.parameters.ShowAlertMessageEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowToastMessageEventParameters
import com.anvipo.angram.layers.core.message.SystemMessage
import com.anvipo.angram.layers.core.message.SystemMessageType
import com.anvipo.angram.layers.global.types.EnabledProxyIdReceiveChannel
import com.anvipo.angram.layers.global.types.SystemMessageReceiveChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionState
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateReceiveChannel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.TdApi

class AppViewModelImpl(
    private val useCaseFactoryMethod: () -> AppUseCase,
    private val coordinatorFactoryMethod: () -> ApplicationCoordinator,
    private val enabledProxyIdReceiveChannel: EnabledProxyIdReceiveChannel,
    private val systemMessageReceiveChannel: SystemMessageReceiveChannel,
    private val tdApiUpdateConnectionStateReceiveChannel: TdApiUpdateConnectionStateReceiveChannel,
    private val tdLibClientHasBeenRecreatedReceiveChannel: TDLibClientHasBeenRecreatedReceiveChannel,
    private val resourceManager: ResourceManager
) : BaseViewModelImpl(), AppViewModel {

    init {
        channelsThatWillBeUnsubscribedInOnDestroy.addAll(
            listOf(
                enabledProxyIdReceiveChannel,
                systemMessageReceiveChannel,
                tdApiUpdateConnectionStateReceiveChannel
            )
        )
    }

    override val setNavigatorEvents: SingleLiveEvent<SetNavigatorEventParameters> = SingleLiveEvent()

    override fun onColdStart() {
        super<BaseViewModelImpl>.onColdStart()
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name

        myLaunch {
            val applicationCoordinator = withContext(Dispatchers.Default) {
                coordinatorFactoryMethod()
            }

            val applicationFlowCoordinateResult = applicationCoordinator.start()
            applicationCoordinator.freeAllResources()

            myLog(
                invokationPlace = invokationPlace,
                text = "applicationFlowCoordinateResult = $applicationFlowCoordinateResult"
            )

            withContext(Dispatchers.IO) {
                for (ping in tdLibClientHasBeenRecreatedReceiveChannel) {
                    onColdStart()
                }
            }
        }
    }

    override fun onResumeFragments() {
        subscribeToChannels()
        setNavigator()
    }

    override fun onPauseTriggered() {
        super<BaseViewModelImpl>.onPauseTriggered()
        removeNavigator()
    }


    private fun setNavigator() {
        setNavigatorEvents.value = SET
    }

    private fun removeNavigator() {
        setNavigatorEvents.value = REMOVE
    }

    private fun subscribeToChannels() {
        subscribeOnSystemMessages()
        subscribeOnConnectionStates()
        subscribeOnEnabledProxyId()
    }

    private fun subscribeOnSystemMessages() {
        myLaunch(
            context = Dispatchers.IO
        ) {
            for (receivedSystemMessage in systemMessageReceiveChannel) {
                onReceivedSystemMessage(receivedSystemMessage)
            }
        }
    }

    private fun subscribeOnConnectionStates() {
        myLaunch(
            context = Dispatchers.IO
        ) {
            for (receivedTdApiUpdateConnectionState in tdApiUpdateConnectionStateReceiveChannel) {
                onReceivedTdApiUpdateConnectionState(receivedTdApiUpdateConnectionState)
            }
        }
    }

    private fun subscribeOnEnabledProxyId() {
        myLaunch(
            context = Dispatchers.IO
        ) {
            for (receivedEnabledProxyId in enabledProxyIdReceiveChannel) {
                onReceivedEnabledProxyId(receivedEnabledProxyId)
            }
        }
    }

    private suspend fun onReceivedSystemMessage(receivedSystemMessage: SystemMessage) {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name
        val (text, type, shouldBeShownToUser, shouldBeShownInLogs) = receivedSystemMessage

        if (shouldBeShownToUser) {
            when (type) {
                SystemMessageType.TOAST -> showToastMessage(
                    ShowToastMessageEventParameters(
                        text = text
                    )
                )
                SystemMessageType.ALERT -> showAlertMessage(
                    ShowAlertMessageEventParameters(
                        title = null,
                        text = text,
                        cancelable = true,
                        messageDialogTag = null
                    )
                )
            }
        }

        if (shouldBeShownInLogs) {
            myLog(
                invokationPlace = invokationPlace,
                text = text
            )
        }
    }

    private fun onReceivedTdApiUpdateConnectionState(
        receivedTdApiUpdateConnectionState: TdApiUpdateConnectionState
    ) {
        val connectionState = resourceManager.getString(R.string.connection_state)
        val text: String
        var duration: Int = Snackbar.LENGTH_INDEFINITE

        when (receivedTdApiUpdateConnectionState.state) {
            is TdApi.ConnectionStateWaitingForNetwork -> {
                text = "$connectionState: waiting for network"
            }
            is TdApi.ConnectionStateConnectingToProxy -> {
                text = "$connectionState: connecting to proxy"
            }
            is TdApi.ConnectionStateConnecting -> {
                text = "$connectionState: connecting"
            }
            is TdApi.ConnectionStateUpdating -> {
                text = "$connectionState: updating"
            }
            is TdApi.ConnectionStateReady -> {
                text = "$connectionState: connected"
                duration = Snackbar.LENGTH_SHORT
            }
            else -> {
                assertionFailure(
                    "Undefined received td api update connection state = $receivedTdApiUpdateConnectionState"
                )
                text = "$connectionState: ERROR"
            }
        }

        myLaunch {
            showConnectionSnackMessage(
                ShowSnackMessageEventParameters(
                    text = text,
                    duration = duration
                )
            )
        }
    }

    private suspend fun onReceivedEnabledProxyId(receivedEnabledProxyId: Int?) {
        withContext(Dispatchers.IO) {
            val useCase = useCaseFactoryMethod()

            useCase.saveEnabledProxyId(receivedEnabledProxyId)
        }
    }

}