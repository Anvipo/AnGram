package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter

import android.content.Context
import android.telephony.TelephonyManager
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.R
import com.anvipo.angram.businessLogicLayer.useCases.authUserStory.enterPhoneNumberUseCase.EnterPhoneNumberUseCase
import com.anvipo.angram.coreLayer.CoreHelpers.debugLog
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.errors.TdApiError
import com.anvipo.angram.presentationLayer.common.baseClasses.BasePresenterImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


@InjectViewState
class EnterPhoneNumberPresenterImp(
    private val useCase: EnterPhoneNumberUseCase,
    private val routeEventHandler: AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler,
    private val resourceManager: ResourceManager
) : BasePresenterImp<EnterPhoneNumberView>(), EnterPhoneNumberPresenter {

    override fun coldStart() {
        viewState.hideNextButton()

        viewState.setMaxLengthOfPhoneNumber(phoneNumberLength.toInt())
    }

    override fun onResumeTriggered() {
        viewState.hideProgress()
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
    }

    private var onNextButtonPressedJob: Job? = null

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

}
