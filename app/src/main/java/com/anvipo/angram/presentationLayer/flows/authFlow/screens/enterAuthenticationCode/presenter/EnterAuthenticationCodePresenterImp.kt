package com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationCode.presenter

import com.anvipo.angram.BuildConfig
import com.anvipo.angram.R
import com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterAuthenticationCodeUseCase.EnterAuthenticationCodeUseCase
import com.anvipo.angram.coreLayer.CoreHelpers.debugLog
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.dataLayer.gateways.tdLib.errors.TdApiError
import com.anvipo.angram.presentationLayer.common.baseClasses.BasePresenterImp
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationCodeRouteEventHandler
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationCode.view.EnterAuthenticationCodeView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")
@InjectViewState
class EnterAuthenticationCodePresenterImp(
    private val routeEventHandler: AuthorizationCoordinatorEnterAuthenticationCodeRouteEventHandler,
    private val useCase: EnterAuthenticationCodeUseCase,
    private val resourceManager: ResourceManager
) : BasePresenterImp<EnterAuthenticationCodeView>(), EnterAuthenticationCodePresenter {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.hideNextButton()
        viewState.hideRegistrationViews()
        viewState.hideProgress()
    }

    override fun onResumeTriggered() {
        if (registrationRequired) {
            viewState.showRegistrationViews()
        }
        viewState.hideProgress()
    }

    override fun onNextButtonPressed(
        enteredAuthenticationCode: CorrectAuthenticationCodeType,
        lastName: String,
        firstName: String
    ) {
        val onNextButtonPressedCEH = CoroutineExceptionHandler { _, error ->
            if (BuildConfig.DEBUG) {
                val errorText = error.localizedMessage
                debugLog(errorText)
                viewState.showErrorAlert(errorText)
            }
        }

        viewState.showProgress()

        launch(
            context = coroutineContext + onNextButtonPressedCEH
        ) {
            useCase
                .checkAuthenticationCodeCatching(
                    enteredAuthenticationCode = enteredAuthenticationCode,
                    lastName = lastName,
                    firstName = firstName
                )
                .onSuccess { routeEventHandler.onEnterCorrectAuthenticationCode() }
                .onFailure { handleAuthenticationCodeFailure(it) }
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    override fun onResendAuthenticationCodeButtonPressed() {
        val onResendAuthenticationCodeButtonPressedCEH = CoroutineExceptionHandler { _, error ->
            if (BuildConfig.DEBUG) {
                val errorText = error.localizedMessage
                debugLog(errorText)
                viewState.showErrorAlert(errorText)
            }
        }

        viewState.showProgress()

        launch(
            context = coroutineContext + onResendAuthenticationCodeButtonPressedCEH
        ) {
            useCase
                .resendAuthenticationCodeCatching()
                .onSuccess { withContext(Dispatchers.Main) { viewState.hideProgress() } }
                .onFailure { handleAuthenticationCodeFailure(it) }
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
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
        routeEventHandler.onPressedBackButtonInEnterAuthenticationCodeScreen()
    }

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    private var enteredPhoneNumber: String = ""
    private var expectedCodeLength: UInt = 10u

    private var authenticationCodeIsEnteredAndCorrect: Boolean = false

    private var registrationRequired: Boolean = false

    private var firstNameIsEntered: Boolean = false
    private var lastNameIsEntered: Boolean = false

    private suspend fun handleAuthenticationCodeFailure(error: Throwable) {
        val errorMessage: String = resourceManager.run {
            if (error is TdApiError) {
                when {
                    error.code == 400 && error.message == "PHONE_CODE_INVALID" ->
                        getString(R.string.code_is_invalid)
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