package com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationCode.presenter

import com.anvipo.angram.R
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterAuthenticationCode.EnterAuthenticationCodeUseCase
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.data.gateways.tdLib.errors.TdApiError
import com.anvipo.angram.layers.presentation.common.baseClasses.BasePresenterImp
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationCodeRouteEventHandler
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationCode.view.EnterAuthenticationCodeView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.Dispatchers

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
        viewState.showProgress()

        myLaunch {
            useCase
                .checkAuthenticationCodeCatching(
                    enteredAuthenticationCode = enteredAuthenticationCode,
                    lastName = lastName,
                    firstName = firstName
                )
                .onSuccess { routeEventHandler.onEnterCorrectAuthenticationCode() }
                .onFailure { handleAuthenticationCodeFailure(it) }
        }
    }

    override fun onResendAuthenticationCodeButtonPressed() {
        viewState.showProgress()

        myLaunch {
            useCase
                .resendAuthenticationCodeCatching()
                .onSuccess { myLaunch { viewState.hideProgress() } }
                .onFailure(::handleAuthenticationCodeFailure)
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
        routeEventHandler.onPressedBackButtonInEnterAuthenticationCodeScreen()
    }


    private var enteredPhoneNumber: String = ""
    private var expectedCodeLength: UInt = 10u

    private var authenticationCodeIsEnteredAndCorrect: Boolean = false

    private var registrationRequired: Boolean = false

    private var firstNameIsEntered: Boolean = false
    private var lastNameIsEntered: Boolean = false

    private fun handleAuthenticationCodeFailure(error: Throwable) {
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

        myLaunch(Dispatchers.Main) {
            viewState.hideProgress()
            viewState.showErrorAlert(errorMessage)
        }
    }

}