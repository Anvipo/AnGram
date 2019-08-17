package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.viewModel

import com.anvipo.angram.R
import com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.enterAuthenticationCode.EnterAuthenticationCodeUseCase
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.core.base.classes.BaseViewModelImpl
import com.anvipo.angram.layers.core.events.SingleLiveEvent
import com.anvipo.angram.layers.core.events.parameters.ShowAlertMessageEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowErrorEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters.HIDE
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters.SHOW
import com.anvipo.angram.layers.data.gateways.tdLib.errors.TdApiError
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationCodeRouteEventHandler
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.types.SetExpectedCodeLengthEventParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EnterAuthenticationCodeViewModelImpl(
    private val routeEventHandler: AuthorizationCoordinatorEnterAuthenticationCodeRouteEventHandler,
    private val useCase: EnterAuthenticationCodeUseCase,
    private val resourceManager: ResourceManager
) : BaseViewModelImpl(), EnterAuthenticationCodeViewModel {

    override val setExpectedCodeLengthEvents: SingleLiveEvent<SetExpectedCodeLengthEventParameters> =
        SingleLiveEvent()
    override val showNextButtonEvents: SingleLiveEvent<ShowViewEventParameters> =
        SingleLiveEvent()
    override val showRegistrationViewsEvents: SingleLiveEvent<ShowViewEventParameters> =
        SingleLiveEvent()

    override fun onCreateTriggered() {
        super<BaseViewModelImpl>.onCreateTriggered()
        hideNextButton()
        hideRegistrationViews()
        hideProgress()
    }

    override fun onResumeTriggered() {
        if (registrationRequired) {
            showRegistrationViews()
        }

        hideProgress()
    }

    override fun onNextButtonPressed(
        enteredAuthenticationCode: CorrectAuthenticationCodeType,
        lastName: String,
        firstName: String
    ) {
        showProgress()

        myLaunch {
            val checkAuthenticationCodeResult = withContext(Dispatchers.IO) {
                useCase
                    .checkAuthenticationCodeCatching(
                        enteredAuthenticationCode = enteredAuthenticationCode,
                        lastName = lastName,
                        firstName = firstName
                    )
            }

            checkAuthenticationCodeResult.onFailure { handleAuthenticationCodeFailure(it) }
        }
    }

    override fun onResendAuthenticationCodeButtonPressed() {
        showProgress()

        myLaunch {
            val resendAuthenticationCodeResult = withContext(Dispatchers.IO) {
                useCase.resendAuthenticationCodeCatching()
            }

            resendAuthenticationCodeResult
                .onSuccess { withContext(Dispatchers.Main) { hideProgress() } }
                .onFailure { handleAuthenticationCodeFailure(it) }
        }
    }

    @ExperimentalUnsignedTypes
    override fun onGetExpectedCodeLength(expectedCodeLength: UInt) {
        this.expectedCodeLength = expectedCodeLength
        setMaxLengthOfEditText(this.expectedCodeLength.toInt())
    }

    override fun onGetEnteredPhoneNumber(enteredPhoneNumber: String) {
        this.enteredPhoneNumber = enteredPhoneNumber
    }

    override fun onGetRegistrationRequired(registrationRequired: Boolean) {
        this.registrationRequired = registrationRequired
    }

    override fun onGetTermsOfServiceText(termsOfServiceText: String) {
        if (termsOfServiceText.isNotBlank() && termsOfServiceText.isNotEmpty()) {
            showAlertMessage(
                ShowAlertMessageEventParameters(
                    title = null,
                    text = termsOfServiceText,
                    cancelable = true,
                    messageDialogTag = null
                )
            )
        }
    }

    @ExperimentalUnsignedTypes
    override fun onAuthenticationCodeTextChanged(text: CharSequence?) {
        if (text.isNullOrBlank()) {
            authenticationCodeIsEnteredAndCorrect = false
            hideNextButton()
            return
        }

        if (text.length.toUInt() < expectedCodeLength) {
            authenticationCodeIsEnteredAndCorrect = false
            hideNextButton()
            return
        }

        authenticationCodeIsEnteredAndCorrect = true

        if (authenticationCodeIsEnteredAndCorrect) {
            if (!registrationRequired) {
                showNextButton()
                return
            }

            if (registrationRequired && firstNameIsEntered && lastNameIsEntered) {
                showNextButton()
            } else {
                hideNextButton()
            }
        }
    }

    override fun onFirstNameTextChanged(text: CharSequence?) {
        if (text.isNullOrBlank()) {
            firstNameIsEntered = false
            hideNextButton()
            return
        }

        firstNameIsEntered = true

        if (authenticationCodeIsEnteredAndCorrect) {
            if (registrationRequired && firstNameIsEntered && lastNameIsEntered) {
                showNextButton()
            } else {
                hideNextButton()
            }
        }
    }

    override fun onLastNameTextChanged(text: CharSequence?) {
        if (text.isNullOrBlank()) {
            lastNameIsEntered = false
            hideNextButton()
            return
        }

        lastNameIsEntered = true

        if (authenticationCodeIsEnteredAndCorrect) {
            if (registrationRequired && firstNameIsEntered && lastNameIsEntered) {
                showNextButton()
            } else {
                hideNextButton()
            }
        }
    }

    override fun onBackPressed() {
        myLaunch {
            routeEventHandler.onPressedBackButtonInEnterAuthenticationCodeScreen()
        }
    }


    private var enteredPhoneNumber: String = ""
    @ExperimentalUnsignedTypes
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

    private fun setMaxLengthOfEditText(expectedCodeLength: Int) {
        setExpectedCodeLengthEvents.value = SetExpectedCodeLengthEventParameters(expectedCodeLength)
    }

    private fun showNextButton() {
        showNextButtonEvents.value = SHOW
    }

    private fun hideNextButton() {
        showNextButtonEvents.value = HIDE
    }

    private fun showRegistrationViews() {
        showRegistrationViewsEvents.value = SHOW
    }

    private fun hideRegistrationViews() {
        showRegistrationViewsEvents.value = HIDE
    }

}