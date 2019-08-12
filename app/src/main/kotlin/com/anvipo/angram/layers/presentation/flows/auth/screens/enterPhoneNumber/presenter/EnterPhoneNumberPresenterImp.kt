package com.anvipo.angram.layers.presentation.flows.auth.screens.enterPhoneNumber.presenter

import com.anvipo.angram.R
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterPhoneNumber.EnterPhoneNumberUseCase
import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.data.gateways.tdLib.errors.TdApiError
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionState
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateReceiveChannel
import com.anvipo.angram.layers.presentation.common.baseClasses.BasePresenterImp
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.interfaces.AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.types.ProxyType
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterPhoneNumber.view.EnterPhoneNumberView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.drinkless.td.libcore.telegram.TdApi

@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")
@InjectViewState
class EnterPhoneNumberPresenterImp(
    private val useCase: EnterPhoneNumberUseCase,
    private val routeEventHandler: AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler,
    private val resourceManager: ResourceManager,
    private val tdApiUpdateConnectionStateReceiveChannel: TdApiUpdateConnectionStateReceiveChannel
) : BasePresenterImp<EnterPhoneNumberView>(), EnterPhoneNumberPresenter {

    init {
        channelsThatWillBeUnsubscribedInOnDestroy.add(tdApiUpdateConnectionStateReceiveChannel)
    }

    override fun coldStart() {
        viewState.hideNextButton()
    }

    override fun onResumeTriggered() {
        viewState.hideProgress()
        subscribeOnConnectionStates()
    }


    override fun onAddProxyButtonPressed() {
        viewState.showItemsDialog(
            resourceManager.getString(R.string.choose_proxy_server_type),
            proxysList
        )
    }

    override fun onItemClicked(index: Int) {
        when (proxys[index]) {
            mtprotoPair.second -> routeEventHandler.onAddProxyButtonTapped(ProxyType.MTPROTO)
        }
    }

    override fun onNextButtonPressed(enteredPhoneNumber: String) {
        viewState.showProgress()

        myLaunch {
            useCase.setAuthenticationPhoneNumberCatching(enteredPhoneNumber)
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

                    myLaunch(Dispatchers.Main) {
                        viewState.hideProgress()
                        viewState.showErrorAlert(errorMessage)
                    }
                }
        }
    }

    override fun onPhoneNumberTextChanged(text: String) {
        if (text.length <= 1) {
            viewState.hideNextButton()
            return
        }

        viewState.showNextButton()
    }

    override fun onBackPressed() {
        routeEventHandler.onPressedBackButtonInEnterPhoneNumberScreen()
    }

    override fun onCanceledProgressDialog() {
        val methodName = object {}.javaClass.enclosingMethod!!.name
        val cancellationException = CancellationException("$className::$methodName")

        onNextButtonPressedJob?.cancel(cancellationException)
        viewState.showSnackMessage(resourceManager.getString(R.string.query_canceled))
    }


    private var onNextButtonPressedJob: Job? = null

    private fun subscribeOnConnectionStates() {
        myLaunch {
            for (receivedTdApiUpdateConnectionState in tdApiUpdateConnectionStateReceiveChannel) {
                onReceivedTdApiUpdateConnectionState(receivedTdApiUpdateConnectionState)
            }
        }
    }

    private fun onReceivedTdApiUpdateConnectionState(receivedTdApiUpdateConnectionState: TdApiUpdateConnectionState) {
        myLaunch(Dispatchers.Main) {
            when (val receivedConnectionState = receivedTdApiUpdateConnectionState.state) {
                is TdApi.ConnectionStateWaitingForNetwork -> viewState.disableNextButton()
                is TdApi.ConnectionStateConnectingToProxy -> viewState.disableNextButton()
                is TdApi.ConnectionStateConnecting -> viewState.disableNextButton()
                is TdApi.ConnectionStateUpdating -> viewState.disableNextButton()
                is TdApi.ConnectionStateReady -> viewState.enableNextButton()
                else -> assertionFailure("Undefined received connection state $receivedConnectionState")
            }
        }
    }

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

}
