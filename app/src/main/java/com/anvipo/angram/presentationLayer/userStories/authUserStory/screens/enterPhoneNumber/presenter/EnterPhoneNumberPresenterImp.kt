package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter

import android.content.Context
import android.telephony.TelephonyManager
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.R
import com.anvipo.angram.applicationLayer.types.ConnectionState
import com.anvipo.angram.applicationLayer.types.ConnectionStateReceiveChannel
import com.anvipo.angram.businessLogicLayer.useCases.authUserStory.enterPhoneNumberUseCase.EnterPhoneNumberUseCase
import com.anvipo.angram.coreLayer.CoreHelpers.debugLog
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.errors.TdApiError
import com.anvipo.angram.presentationLayer.common.baseClasses.BasePresenterImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.addProxy.types.ProxyType
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")
@InjectViewState
class EnterPhoneNumberPresenterImp(
    private val useCase: EnterPhoneNumberUseCase,
    private val routeEventHandler: AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler,
    private val resourceManager: ResourceManager,
    private val connectionStateReceiveChannel: ConnectionStateReceiveChannel
) : BasePresenterImp<EnterPhoneNumberView>(), EnterPhoneNumberPresenter {

    override fun coldStart() {
        viewState.hideNextButton()

        viewState.setMaxLengthOfPhoneNumber(phoneNumberLength.toInt())
    }

    override fun onResumeTriggered() {
        viewState.hideProgress()
        subscribeOnConnectionStates()
    }

    override fun onPauseTriggered() {
        connectionStateReceiveChannel.cancel(CancellationException("EnterPhoneNumberView onPause"))
    }

    override fun onAddProxyButtonPressed() {
        viewState.showItemsDialog(
            "Выберите нужный тип прокси сервера",
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
        val onNextButtonPressedCoroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
            if (BuildConfig.DEBUG) {
                val errorText = error.localizedMessage
                debugLog(errorText)
                viewState.showErrorAlert(errorText)
            }
        }

        viewState.showProgress()

        onNextButtonPressedJob = launch(
            context = coroutineContext + onNextButtonPressedCoroutineExceptionHandler
        ) {
            useCase.setAuthenticationPhoneNumberCatching(enteredPhoneNumber)
                .onSuccess {
                    routeEventHandler.onEnterCorrectPhoneNumber()
                }
                .onFailure { error ->
                    val errorMessage: String = resourceManager.run {
                        when (error) {
                            is TdApiError.Custom.EmptyParameter ->
                                getString(R.string.phone_number_cant_be_empty)
                            is TdApiError.Custom.BadRequest ->
                                getString(R.string.phone_number_invalid)
                            is TdApiError.Custom.TooManyRequests ->
                                getString(R.string.too_many_requests_try_later)
                            else -> getString(R.string.unknown_error)
                        }
                    }

                    withContext(Dispatchers.Main) {
                        viewState.hideProgress()
                        viewState.showErrorAlert(errorMessage)
                    }
                }
        }
    }

    override fun onPhoneNumberTextChanged(text: CharSequence?) {
        if (text == null) {
            viewState.hideNextButton()
            return
        }

        if (text.length.toUInt() < phoneNumberLength) {
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

    override fun cancelAllJobs() {
        onNextButtonPressedJob?.cancel()
        receiveFirstConnectionStatesJob?.cancel()
        receiveConnectionStatesJob?.cancel()
        doOnNewConnectionStateJob?.cancel()
    }

    private var onNextButtonPressedJob: Job? = null
    private var receiveFirstConnectionStatesJob: Job? = null
    private var receiveConnectionStatesJob: Job? = null
    private var doOnNewConnectionStateJob: Job? = null

    private val phoneNumberLength: UInt
        get() {
            val telephonyManager =
                resourceManager.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            return when (telephonyManager.simCountryIso) {
                "ru" -> 12u
                // TODO: other countries
                else -> 12u
            }
        }

    private fun subscribeOnConnectionStates() {
        val receiveConnectionStatesCoroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
            if (BuildConfig.DEBUG) {
                val text = error.localizedMessage
                debugLog(text)
                viewState.showErrorAlert(text)
            }
        }

        receiveConnectionStatesJob = launch(
            context = coroutineContext + receiveConnectionStatesCoroutineExceptionHandler
        ) {
            val receivedConnectionState = connectionStateReceiveChannel.receive()

            if (receivedConnectionState == ConnectionState.Undefined) {
                val errorText = "receivedConnectionState == Undefined"
                debugLog(errorText)
                if (BuildConfig.DEBUG) {
                    viewState.showToastMessage(errorText)
                }

                return@launch
            }

            val block: () -> Unit = {
                when (receivedConnectionState) {
                    ConnectionState.WaitingForNetwork -> {
                        TODO()
                    }
                    ConnectionState.ConnectingToProxy -> {
                        TODO()
                    }
                    ConnectionState.Connecting -> {
                        viewState.disableNextButton()
                    }
                    ConnectionState.Updating -> {
                        TODO()
                    }
                    ConnectionState.Ready -> {
                        TODO()
                    }
                    ConnectionState.Undefined -> {
                        TODO()
                    }
                }
            }

            val doOnNewConnectionStateCEH = CoroutineExceptionHandler { _, error ->
                if (BuildConfig.DEBUG) {
                    val errorText = error.localizedMessage
                    debugLog(errorText)
                    viewState.showErrorAlert(errorText)
                }
            }

            doOnNewConnectionStateJob = launch(
                context = Dispatchers.Main + doOnNewConnectionStateCEH
            ) {
                block()
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
