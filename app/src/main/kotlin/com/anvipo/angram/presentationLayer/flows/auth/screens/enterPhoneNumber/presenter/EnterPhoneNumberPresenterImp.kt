package com.anvipo.angram.presentationLayer.flows.auth.screens.enterPhoneNumber.presenter

import com.anvipo.angram.BuildConfig
import com.anvipo.angram.R
import com.anvipo.angram.layers.application.types.ConnectionStateReceiveChannel
import com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterPhoneNumber.EnterPhoneNumberUseCase
import com.anvipo.angram.coreLayer.CoreHelpers.assertionFailure
import com.anvipo.angram.coreLayer.CoreHelpers.debugLog
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.dataLayer.gateways.tdLib.errors.TdApiError
import com.anvipo.angram.presentationLayer.common.baseClasses.BasePresenterImp
import com.anvipo.angram.presentationLayer.flows.auth.coordinator.interfaces.AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler
import com.anvipo.angram.presentationLayer.flows.auth.screens.addProxy.types.ProxyType
import com.anvipo.angram.presentationLayer.flows.auth.screens.enterPhoneNumber.view.EnterPhoneNumberView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.*
import org.drinkless.td.libcore.telegram.TdApi
import kotlin.coroutines.CoroutineContext

@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")
@InjectViewState
class EnterPhoneNumberPresenterImp(
    private val useCase: EnterPhoneNumberUseCase,
    private val routeEventHandler: AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler,
    private val resourceManager: ResourceManager,
    private val connectionStateReceiveChannel: ConnectionStateReceiveChannel
) : BasePresenterImp<EnterPhoneNumberView>(), EnterPhoneNumberPresenter {

    init {
        val receiveChannelList = listOf(
            connectionStateReceiveChannel
        )

        channelsThatWillBeUnsubscribedInOnPause.addAll(receiveChannelList)
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
            mtprotoPair.second -> {
                routeEventHandler.onAddProxyButtonTapped(ProxyType.MTPROTO)
            }
        }
    }

    override fun onNextButtonPressed(enteredPhoneNumber: String) {
        val onNextButtonPressedCEH = CoroutineExceptionHandler { _, error ->
            if (BuildConfig.DEBUG) {
                val errorText = error.localizedMessage
                debugLog(errorText)
                viewState.showErrorAlert(errorText)
            }
        }

        viewState.showProgress()

        onNextButtonPressedJob = launch(
            context = coroutineContext + onNextButtonPressedCEH
        ) {
            useCase.setAuthenticationPhoneNumberCatching(enteredPhoneNumber)
                .onSuccess {
                    routeEventHandler.onEnterCorrectPhoneNumber()
                }
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
        onNextButtonPressedJob?.cancel()
        viewState.showSnackMessage(resourceManager.getString(R.string.query_canceled))
    }

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    private var onNextButtonPressedJob: Job? = null

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
            for (receivedConnectionState in connectionStateReceiveChannel) {
                onReceivedConnectionState(receivedConnectionState)
            }
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    private fun onReceivedConnectionState(receivedConnectionState: TdApi.ConnectionState) {
        val doOnNewConnectionStateCEH = CoroutineExceptionHandler { _, error ->
            if (BuildConfig.DEBUG) {
                val errorText = error.localizedMessage
                debugLog(errorText)
                viewState.showErrorAlert(errorText)
            }
        }

        launch(
            context = Dispatchers.Main + doOnNewConnectionStateCEH
        ) {
            when (receivedConnectionState) {
                is TdApi.ConnectionStateWaitingForNetwork -> viewState.disableNextButton()
                is TdApi.ConnectionStateConnectingToProxy -> viewState.disableNextButton()
                is TdApi.ConnectionStateConnecting -> viewState.disableNextButton()
                is TdApi.ConnectionStateUpdating -> viewState.disableNextButton()
                is TdApi.ConnectionStateReady -> viewState.enableNextButton()
                else -> assertionFailure()
            }
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
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
