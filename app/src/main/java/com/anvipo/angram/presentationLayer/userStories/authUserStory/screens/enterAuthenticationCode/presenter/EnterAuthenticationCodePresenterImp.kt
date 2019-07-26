package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationCode.presenter

import com.anvipo.angram.BuildConfig
import com.anvipo.angram.R
import com.anvipo.angram.businessLogicLayer.useCases.enterAuthenticationCodeUseCase.EnterAuthenticationCodeUseCase
import com.anvipo.angram.coreLayer.CoreHelpers.debugLog
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.errors.TdApiError
import com.anvipo.angram.presentationLayer.common.baseClasses.BasePresenterImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorEnterAuthCodeOutput
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationCode.view.EnterAuthenticationCodeView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@InjectViewState
class EnterAuthenticationCodePresenterImp(
    override val coordinator: AuthorizationCoordinatorEnterAuthCodeOutput,
    override val useCase: EnterAuthenticationCodeUseCase,
    private val resourceManager: ResourceManager
) : BasePresenterImp<EnterAuthenticationCodeView>(), EnterAuthenticationCodePresenter {

    override fun coldStart() {
        viewState.hideNextButton()
        viewState.hideRegistrationViews()
    }

    override fun onStartTriggered() {
        if (registrationRequired) {
            viewState.showRegistrationViews()
        }
    }

    override fun onNextButtonPressed(
        enteredAuthenticationCode: CorrectAuthenticationCodeType,
        lastName: String,
        firstName: String
    ) {
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
            useCase.checkAuthenticationCodeCatching(
                enteredAuthenticationCode = enteredAuthenticationCode,
                lastName = lastName,
                firstName = firstName
            )
                .onSuccess {
                    coordinator.onEnterCorrectAuthCode()
                }
                .onFailure { error ->
                    val errorMessage: String = resourceManager.run {
                        when (error) {
                            is TdApiError.Custom.EmptyParameter -> getString(R.string.unknown_error)
                            is TdApiError.Custom.BadRequest -> {
                                if (error.message == "PHONE_CODE_INVALID") {
                                    getString(R.string.code_is_invalid)
                                } else {
                                    getString(R.string.unknown_error)
                                }
                            }
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

    override fun onResendAuthenticationCodeButtonPressed() {
        val onResendAuthenticationCodeButtonPressedCoroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
            if (BuildConfig.DEBUG) {
                val errorText = error.localizedMessage
                debugLog(errorText)
                viewState.showErrorAlert(errorText)
            }
        }

        viewState.showProgress()

        onResendAuthenticationCodeButtonPressedJob = launch(
            context = coroutineContext + onResendAuthenticationCodeButtonPressedCoroutineExceptionHandler
        ) {
            useCase.resendAuthenticationCodeCatching()
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        viewState.hideProgress()
                    }
                }
                .onFailure { error ->
                    val errorMessage: String = resourceManager.run {
                        when (error) {
                            is TdApiError.Custom.EmptyParameter -> getString(R.string.unknown_error)
                            is TdApiError.Custom.BadRequest -> {
                                if (error.message == "PHONE_CODE_INVALID") {
                                    getString(R.string.code_is_invalid)
                                } else {
                                    getString(R.string.unknown_error)
                                }
                            }
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

    override fun onGetExpectedCodeLength(expectedCodeLength: UInt) {
        this.expectedCodeLength = expectedCodeLength
        viewState.setMaxLengthOfEditText(this.expectedCodeLength.toInt())
    }

    override fun onGetEnteredPhoneNumber(enteredPhoneNumber: String) {
        this.enteredPhoneNumber = enteredPhoneNumber
    }

    override fun onGetRegistrationRequired(registrationRequired: Boolean) {
        this.registrationRequired = registrationRequired
    }

    override fun onGetTermsOfServiceText(termsOfServiceText: String) {
        if (termsOfServiceText.isNotBlank() && termsOfServiceText.isNotEmpty()) {
            viewState.showAlertMessage(termsOfServiceText)
        }
    }

    override fun onAuthenticationCodeTextChanged(text: CharSequence?) {
        if (text.isNullOrBlank()) {
            authenticationCodeIsEnteredAndCorrect = false
            viewState.hideNextButton()
            return
        }

        if (text.length.toUInt() < expectedCodeLength) {
            authenticationCodeIsEnteredAndCorrect = false
            viewState.hideNextButton()
            return
        }

        authenticationCodeIsEnteredAndCorrect = true

        if (authenticationCodeIsEnteredAndCorrect) {
            if (!registrationRequired) {
                viewState.showNextButton()
                return
            }

            if (registrationRequired && firstNameIsEntered && lastNameIsEntered) {
                viewState.showNextButton()
            } else {
                viewState.hideNextButton()
            }
        }
    }

    override fun onFirstNameTextChanged(text: CharSequence?) {
        if (text.isNullOrBlank()) {
            firstNameIsEntered = false
            viewState.hideNextButton()
            return
        }

        firstNameIsEntered = true

        if (authenticationCodeIsEnteredAndCorrect) {
            if (registrationRequired && firstNameIsEntered && lastNameIsEntered) {
                viewState.showNextButton()
            } else {
                viewState.hideNextButton()
            }
        }
    }

    override fun onLastNameTextChanged(text: CharSequence?) {
        if (text.isNullOrBlank()) {
            lastNameIsEntered = false
            viewState.hideNextButton()
            return
        }

        lastNameIsEntered = true

        if (authenticationCodeIsEnteredAndCorrect) {
            if (registrationRequired && firstNameIsEntered && lastNameIsEntered) {
                viewState.showNextButton()
            } else {
                viewState.hideNextButton()
            }
        }
    }

    override fun onBackPressed() {
        coordinator.onPressedBackButtonInEnterAuthenticationCodeScreen()
    }

    override fun cancelAllJobs() {
        onNextButtonPressedJob?.cancel()
        onResendAuthenticationCodeButtonPressedJob?.cancel()
    }

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    private var onResendAuthenticationCodeButtonPressedJob: Job? = null
    private var onNextButtonPressedJob: Job? = null

    private var enteredPhoneNumber: String = ""
    private var expectedCodeLength: UInt = 10u

    private var authenticationCodeIsEnteredAndCorrect: Boolean = false

    private var registrationRequired: Boolean = false

    private var firstNameIsEntered: Boolean = false
    private var lastNameIsEntered: Boolean = false

}