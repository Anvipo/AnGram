package com.anvipo.angram.layers.application.launchSystem.appActivity.presenter

import com.anvipo.angram.R
import com.anvipo.angram.layers.application.coordinator.ApplicationCoordinator
import com.anvipo.angram.layers.application.launchSystem.appActivity.view.AppView
import com.anvipo.angram.layers.businessLogic.useCases.app.AppUseCase
import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.core.base.classes.BasePresenterImp
import com.anvipo.angram.layers.core.message.SystemMessage
import com.anvipo.angram.layers.core.message.SystemMessageType
import com.anvipo.angram.layers.global.types.*
import com.arellomobile.mvp.InjectViewState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.TdApi

@InjectViewState
class AppPresenterImp(
    private val useCase: AppUseCase,
    private val coordinatorFactoryMethod: () -> ApplicationCoordinator,
    private val enabledProxyIdReceiveChannel: EnabledProxyIdReceiveChannel,
    private val systemMessageReceiveChannel: SystemMessageReceiveChannel,
    private val tdApiUpdateConnectionStateReceiveChannel: TdApiUpdateConnectionStateReceiveChannel,
    private val tdLibClientHasBeenRecreatedReceiveChannel: TDLibClientHasBeenRecreatedReceiveChannel,
    private val resourceManager: ResourceManager
) : BasePresenterImp<AppView>(), AppPresenter {

    init {
        channelsThatWillBeUnsubscribedInOnDestroy.addAll(
            listOf(
                enabledProxyIdReceiveChannel,
                systemMessageReceiveChannel,
                tdApiUpdateConnectionStateReceiveChannel
            )
        )
    }

    override fun coldStart() {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name

        myLaunch {
            val applicationCoordinator = withContext(Dispatchers.Default) {
                coordinatorFactoryMethod()
            }

            val applicationFlowCoordinateResult = applicationCoordinator.start()

            myLog(
                invokationPlace = invokationPlace,
                text = "applicationFlowCoordinateResult = $applicationFlowCoordinateResult"
            )

            withContext(Dispatchers.IO) {
                for (tdLibClientHasBeenRecreatedPing in tdLibClientHasBeenRecreatedReceiveChannel) {
                    coldStart()
                }
            }
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
            withContext(Dispatchers.Main) {
                when (type) {
                    SystemMessageType.TOAST -> viewState.showToastMessage(text)
                    SystemMessageType.ALERT -> viewState.showAlertMessage(text)
                }
            }
        }

        if (shouldBeShownInLogs) {
            myLog(
                invokationPlace = invokationPlace,
                text = text
            )
        }
    }

    private suspend fun onReceivedTdApiUpdateConnectionState(
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

        withContext(Dispatchers.Main) {
            viewState.showConnectionStateSnackMessage(
                text = text,
                duration = duration
            )
        }
    }

    private suspend fun onReceivedEnabledProxyId(receivedEnabledProxyId: Int?) {
        withContext(Dispatchers.IO) {
            useCase.saveEnabledProxyId(receivedEnabledProxyId)
        }
    }

}