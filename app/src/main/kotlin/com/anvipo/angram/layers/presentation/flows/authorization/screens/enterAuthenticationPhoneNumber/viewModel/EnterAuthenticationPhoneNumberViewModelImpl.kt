package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.viewModel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anvipo.angram.R
import com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.enterAuthenticationPhoneNumber.EnterAuthenticationPhoneNumberUseCase
import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.core.ShowItemsDialogEventParameters
import com.anvipo.angram.layers.core.ShowSnackMessageEventParameters
import com.anvipo.angram.layers.core.base.classes.BaseViewModelImpl
import com.anvipo.angram.layers.core.events.SingleLiveEvent
import com.anvipo.angram.layers.core.events.parameters.EnableViewEventsParameters
import com.anvipo.angram.layers.core.events.parameters.EnableViewEventsParameters.DISABLE
import com.anvipo.angram.layers.core.events.parameters.EnableViewEventsParameters.ENABLE
import com.anvipo.angram.layers.core.events.parameters.ShowErrorEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters.HIDE
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters.SHOW
import com.anvipo.angram.layers.data.gateways.tdLib.errors.TdApiError
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionState
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateReceiveChannel
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationPhoneNumberRouteEventHandler
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.types.ProxyType
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.view.EnterAuthenticationPhoneNumberFragment.Companion.ENTERED_PHONE_NUMBER_KEY
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.TdApi

class EnterAuthenticationPhoneNumberViewModelImpl(
    private val useCase: EnterAuthenticationPhoneNumberUseCase,
    private val routeEventHandler: AuthorizationCoordinatorEnterAuthenticationPhoneNumberRouteEventHandler,
    private val resourceManager: ResourceManager,
    private val tdApiUpdateConnectionStateReceiveChannel: TdApiUpdateConnectionStateReceiveChannel
) : BaseViewModelImpl(), EnterAuthenticationPhoneNumberViewModel {

    init {
        channelsThatWillBeUnsubscribedInOnDestroy.add(tdApiUpdateConnectionStateReceiveChannel)
    }

    override val showNextButtonEvents: LiveData<ShowViewEventParameters> by lazy {
        _showNextButtonEvents
    }
    override val savedEnterPhoneNumberEvents: LiveData<String?> by lazy {
        _savedEnterPhoneNumberEvents
    }
    override val enableNextButtonEvents: LiveData<EnableViewEventsParameters> by lazy {
        _enableNextButtonEvents
    }

    override fun onColdStart() {
        super<BaseViewModelImpl>.onColdStart()
        hideNextButton()
    }

    override fun onHotStart(savedInstanceState: Bundle) {
        super<BaseViewModelImpl>.onHotStart(savedInstanceState)
        val enteredPhoneNumber = savedInstanceState.getString(ENTERED_PHONE_NUMBER_KEY)
        _savedEnterPhoneNumberEvents.value = enteredPhoneNumber
    }

    override fun onResumeTriggered() {
        myLaunch {
            hideProgress()
        }
        subscribeOnConnectionStates()
    }


    override fun onAddProxyButtonPressed() {
        myLaunch {
            showItemsDialog(
                ShowItemsDialogEventParameters(
                    title = resourceManager.getString(R.string.choose_proxy_server_type),
                    items = proxysList,
                    tag = null,
                    cancelable = true
                )
            )
        }
    }

    override fun onItemClicked(index: Int) {
        myLaunch {
            when (proxys[index]) {
                mtprotoPair.second ->
                    routeEventHandler.onAddProxyButtonTapped(ProxyType.MTPROTO)
            }
        }
    }

    override fun onNextButtonPressed(enteredPhoneNumber: String) {
        myLaunch {
            showProgress()

            val setAuthenticationPhoneNumberResult =
                withContext(Dispatchers.IO) {
                    useCase.setAuthenticationPhoneNumberCatching(enteredPhoneNumber)
                }

            setAuthenticationPhoneNumberResult
                .onFailure { error ->
                    val errorMessage: String = resourceManager.run {
                        if (error is TdApiError) {
                            when (error.code) {
                                8 -> getString(R.string.phone_number_cant_be_empty)
                                400 -> getString(R.string.phone_number_invalid)
                                429 -> getString(R.string.too_many_requests_try_later)
                                else -> getString(R.string.unknown_error)
                            }
                        } else {
                            getString(R.string.unknown_error)
                        }
                    }

                    hideProgress()
                    showErrorAlert(
                        ShowErrorEventParameters(
                            text = errorMessage,
                            cancelable = true,
                            messageDialogTag = null
                        )
                    )
                }
        }
    }

    override fun onPhoneNumberTextChanged(text: String) {
        if (text.length <= 1) {
            hideNextButton()
            return
        }

        showNextButton()
    }

    override fun onBackPressed() {
        myLaunch {
            routeEventHandler.onPressedBackButtonInEnterAuthenticationPhoneNumberScreen()
        }
    }

    override fun onCanceledProgressDialog() {
        val methodName = object {}.javaClass.enclosingMethod!!.name
        val cancellationException = CancellationException("$className::$methodName")

        onNextButtonPressedJob?.cancel(cancellationException)
        myLaunch {
            showSnackMessage(
                ShowSnackMessageEventParameters(
                    text = resourceManager.getString(R.string.query_canceled)
                )
            )
        }
    }


    private var onNextButtonPressedJob: Job? = null

    private val mtprotoPair = 0 to "MTPROTO"

    private val _enableNextButtonEvents = MutableLiveData<EnableViewEventsParameters>()
    private val _savedEnterPhoneNumberEvents = MutableLiveData<String?>()
    private val _showNextButtonEvents = MutableLiveData<ShowViewEventParameters>()

    private val proxysList: List<String>
        get() {
            val result = arrayOfNulls<String>(proxys.size)

            for ((proxyIndex, proxyName) in proxys) {
                result[proxyIndex] = proxyName
            }

            return result.filterNotNull()
        }

    private val proxys: Map<Int, String> = mapOf(
        mtprotoPair
    )

    private fun subscribeOnConnectionStates() {
        myLaunch(Dispatchers.IO) {
            for (receivedTdApiUpdateConnectionState in tdApiUpdateConnectionStateReceiveChannel) {
                onReceivedTdApiUpdateConnectionState(receivedTdApiUpdateConnectionState)
            }
        }
    }

    private suspend fun onReceivedTdApiUpdateConnectionState(receivedTdApiUpdateConnectionState: TdApiUpdateConnectionState) {
        withContext(Dispatchers.Main) {
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

    private fun showNextButton() {
        _showNextButtonEvents.value = SHOW
    }

    private fun hideNextButton() {
        _showNextButtonEvents.value = HIDE
    }

    private fun enableNextButton() {
        _enableNextButtonEvents.value = ENABLE
    }

    private fun disableNextButton() {
        _enableNextButtonEvents.value = DISABLE
    }

}
