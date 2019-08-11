package com.anvipo.angram.layers.application.launchSystem.appActivity.presenter

import com.anvipo.angram.R
import com.anvipo.angram.layers.application.coordinator.ApplicationCoordinator
import com.anvipo.angram.layers.application.launchSystem.appActivity.view.AppView
import com.anvipo.angram.layers.application.types.ConnectionStateReceiveChannel
import com.anvipo.angram.layers.application.types.EnabledProxyIdReceiveChannel
import com.anvipo.angram.layers.application.types.SystemMessageReceiveChannel
import com.anvipo.angram.layers.businessLogic.useCases.app.AppUseCase
import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.core.message.SystemMessage
import com.anvipo.angram.layers.core.message.SystemMessageType
import com.anvipo.angram.layers.presentation.common.baseClasses.BasePresenterImp
import com.arellomobile.mvp.InjectViewState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import org.drinkless.td.libcore.telegram.TdApi

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

        myLaunch {
            val applicationFlowCoordinateResult = coordinator.start()

            myLog(
                invokationPlace = invokationPlace,
                currentParameters = "applicationFlowCoordinateResult = $applicationFlowCoordinateResult"
            )
        }
    }

    override fun onResumeFragments() {
        subscribeToChannels()
        viewState.setNavigator()
    }

    override fun onPauseTriggered() {
        super<BasePresenterImp>.onPauseTriggered()
        viewState.removeNavigator()
    }


    private fun subscribeToChannels() {
        subscribeOnSystemMessages()
        subscribeOnConnectionStates()
        subscribeOnEnabledProxyId()
    }

    private fun subscribeOnSystemMessages() {
        myLaunch {
            for (receivedSystemMessage in systemMessageReceiveChannel) {
                onReceivedSystemMessage(receivedSystemMessage)
            }
        }
    }

    private fun subscribeOnConnectionStates() {
        myLaunch {
            for (receivedConnectionState in connectionStateReceiveChannel) {
                onReceivedConnectionState(receivedConnectionState)
            }
        }
    }

    private fun subscribeOnEnabledProxyId() {
        myLaunch {
            for (receivedEnabledProxyId in enabledProxyIdReceiveChannel) {
                onReceivedEnabledProxyId(receivedEnabledProxyId)
            }
        }
    }

    private fun onReceivedSystemMessage(receivedSystemMessage: SystemMessage) {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name
        val (text, type, shouldBeShownToUser, shouldBeShownInLogs) = receivedSystemMessage

        if (shouldBeShownToUser) {
            myLaunch(Dispatchers.Main) {
                when (type) {
                    SystemMessageType.TOAST -> viewState.showToastMessage(text)
                    SystemMessageType.ALERT -> viewState.showAlertMessage(text)
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
        var duration: Int = Snackbar.LENGTH_INDEFINITE

        when (receivedConnectionState) {
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
                assertionFailure("Undefined received connection state = $receivedConnectionState")
                text = "$connectionState: ERROR"
            }
        }

        myLaunch(Dispatchers.Main) {
            viewState.showConnectionStateSnackMessage(
                text = text,
                duration = duration
            )
        }
    }

    private suspend fun onReceivedEnabledProxyId(receivedEnabledProxyId: Int?) {
        useCase.saveEnabledProxyId(receivedEnabledProxyId)
    }

}