package com.anvipo.angram.layers.presentation.flows.authorization.screens.base.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anvipo.angram.layers.application.tdApiHelper.TdApiHelper
import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.core.base.classes.BaseViewModelImpl
import com.anvipo.angram.layers.core.events.parameters.EnableViewEventsParameters
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateReceiveChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.TdApi

abstract class BaseAuthorizationViewModelImpl(
    private val tdApiUpdateConnectionStateReceiveChannel: TdApiUpdateConnectionStateReceiveChannel
) : BaseViewModelImpl(),
    BaseAuthorizationFlowViewModel {

    final override val showNextButtonEvents: LiveData<ShowViewEventParameters> by lazy {
        _showNextButtonEvents
    }
    final override val enableNextButtonEvents: LiveData<EnableViewEventsParameters> by lazy {
        _enableNextButtonEvents
    }

    override fun onColdStart() {
        super<BaseViewModelImpl>.onColdStart()
        hideNextButton()
    }

    override fun onResumeTriggered() {
        super<BaseViewModelImpl>.onResumeTriggered()

        val lastConnectionState = TdApiHelper.lastConnectionState

        if (lastConnectionState is TdApi.ConnectionStateReady) {
            enableNextButton()
        } else {
            disableNextButton()
        }

        subscribeOnConnectionStates()
    }


    protected fun showNextButton() {
        _showNextButtonEvents.value = ShowViewEventParameters.SHOW
    }

    protected fun hideNextButton() {
        _showNextButtonEvents.value = ShowViewEventParameters.HIDE
    }


    @Suppress("MemberVisibilityCanBePrivate")
    protected fun enableNextButton() {
        _enableNextButtonEvents.value = EnableViewEventsParameters.ENABLE
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected fun disableNextButton() {
        _enableNextButtonEvents.value = EnableViewEventsParameters.DISABLE
    }


    private val _enableNextButtonEvents = MutableLiveData<EnableViewEventsParameters>()
    private val _showNextButtonEvents = MutableLiveData<ShowViewEventParameters>()

    private fun subscribeOnConnectionStates() {
        myLaunch(Dispatchers.IO) {
            for (receivedTdApiUpdateConnectionState in tdApiUpdateConnectionStateReceiveChannel) {
                withContext(Dispatchers.Main) {
                    onReceivedTdApiUpdateConnectionState(receivedTdApiUpdateConnectionState)
                }
            }
        }
    }

    private fun onReceivedTdApiUpdateConnectionState(
        receivedTdApiUpdateConnectionState: TdApi.UpdateConnectionState
    ) {
        when (val receivedConnectionState = receivedTdApiUpdateConnectionState.state) {
            is TdApi.ConnectionStateWaitingForNetwork -> disableNextButton()
            is TdApi.ConnectionStateConnectingToProxy -> disableNextButton()
            is TdApi.ConnectionStateConnecting -> disableNextButton()
            is TdApi.ConnectionStateUpdating -> disableNextButton()
            is TdApi.ConnectionStateReady -> enableNextButton()
            else -> assertionFailure("Undefined received connection state $receivedConnectionState")
        }
    }

}