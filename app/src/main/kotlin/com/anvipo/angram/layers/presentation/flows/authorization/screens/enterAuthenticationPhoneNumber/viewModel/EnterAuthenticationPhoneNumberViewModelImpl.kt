package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.viewModel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anvipo.angram.R
import com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.enterAuthenticationPhoneNumber.EnterAuthenticationPhoneNumberUseCase
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.core.ShowItemsDialogEventParameters
import com.anvipo.angram.layers.core.ShowSnackMessageEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowErrorEventParameters
import com.anvipo.angram.layers.data.gateways.tdLib.errors.TdApiError
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateReceiveChannel
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationPhoneNumberRouteEventHandler
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.types.ProxyType
import com.anvipo.angram.layers.presentation.flows.authorization.screens.base.viewModel.BaseAuthorizationViewModelImpl
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.types.EnterAuthenticationPhoneNumberScreenSavedInputData
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.view.EnterAuthenticationPhoneNumberFragment.Companion.ENTERED_PHONE_NUMBER
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext

class EnterAuthenticationPhoneNumberViewModelImpl(
    private val useCase: EnterAuthenticationPhoneNumberUseCase,
    private val routeEventHandler: AuthorizationCoordinatorEnterAuthenticationPhoneNumberRouteEventHandler,
    private val resourceManager: ResourceManager,
    tdApiUpdateConnectionStateReceiveChannel: TdApiUpdateConnectionStateReceiveChannel
) : BaseAuthorizationViewModelImpl(
    tdApiUpdateConnectionStateReceiveChannel
), EnterAuthenticationPhoneNumberViewModel {

    init {
        channelsThatWillBeUnsubscribedInOnDestroy.add(tdApiUpdateConnectionStateReceiveChannel)
    }

    override val enterAuthenticationPhoneNumberScreenSavedInputDataEvents: LiveData<EnterAuthenticationPhoneNumberScreenSavedInputData> by lazy {
        _enterAuthenticationPhoneNumberScreenSavedInputDataEvents
    }

    override fun onHotStart(savedInstanceState: Bundle) {
        super<BaseAuthorizationViewModelImpl>.onHotStart(savedInstanceState)
        val enteredPhoneNumber = savedInstanceState.getString(ENTERED_PHONE_NUMBER)
        _enterAuthenticationPhoneNumberScreenSavedInputDataEvents.value =
            EnterAuthenticationPhoneNumberScreenSavedInputData(
                authenticationPhoneNumber = enteredPhoneNumber
            )
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

    private val _enterAuthenticationPhoneNumberScreenSavedInputDataEvents =
        MutableLiveData<EnterAuthenticationPhoneNumberScreenSavedInputData>()

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
