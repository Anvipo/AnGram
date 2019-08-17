package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.viewModel

import com.anvipo.angram.R
import com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.enterAuthenticationPhoneNumber.EnterAuthenticationPhoneNumberUseCase
import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.core.ShowItemsDialogEvent
import com.anvipo.angram.layers.core.base.classes.BaseViewModelImpl
import com.anvipo.angram.layers.core.events.EnableViewEventsParameters
import com.anvipo.angram.layers.core.events.EnableViewEventsParameters.DISABLE
import com.anvipo.angram.layers.core.events.EnableViewEventsParameters.ENABLE
import com.anvipo.angram.layers.core.events.ShowErrorEventParameters
import com.anvipo.angram.layers.core.events.ShowViewEventParameters
import com.anvipo.angram.layers.core.events.ShowViewEventParameters.HIDE
import com.anvipo.angram.layers.core.events.ShowViewEventParameters.SHOW
import com.anvipo.angram.layers.core.events.SingleLiveEvent
import com.anvipo.angram.layers.data.gateways.tdLib.errors.TdApiError
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionState
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateReceiveChannel
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationPhoneNumberRouteEventHandler
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.types.ProxyType
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

    override val showNextButtonEvents: SingleLiveEvent<ShowViewEventParameters> =
        SingleLiveEvent()
    override val enableNextButtonEvents: SingleLiveEvent<EnableViewEventsParameters> =
        SingleLiveEvent()

    override fun onCreateTriggered() {
        super<BaseViewModelImpl>.onCreateTriggered()
        hideNextButton()
    }

    override fun onResumeTriggered() {
        hideProgress()
        subscribeOnConnectionStates()
    }


    override fun onAddProxyButtonPressed() {
        showItemsDialog(
            ShowItemsDialogEvent(
                title = resourceManager.getString(R.string.choose_proxy_server_type),
                items = proxysList,
                tag = null,
                cancelable = true
            )
        )
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
        showProgress()

        myLaunch {
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

                    withContext(Dispatchers.Main) {
                        hideProgress()
                        showErrorAlert(
                            ShowErrorEventParameters(
                                text = errorMessage
                            )
                        )
                    }
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
        showSnackMessage(resourceManager.getString(R.string.query_canceled))
    }


    private var onNextButtonPressedJob: Job? = null

    private val mtprotoPair = 0 to "MTPROTO"

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
        showNextButtonEvents.value = SHOW
    }

    private fun hideNextButton() {
        showNextButtonEvents.value = HIDE
    }

    private fun enableNextButton() {
        enableNextButtonEvents.value = ENABLE
    }

    private fun disableNextButton() {
        enableNextButtonEvents.value = DISABLE
    }

}
