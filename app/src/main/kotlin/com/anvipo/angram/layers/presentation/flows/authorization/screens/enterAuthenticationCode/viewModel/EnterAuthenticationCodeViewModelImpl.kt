package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.viewModel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anvipo.angram.R
import com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.enterAuthenticationCode.EnterAuthenticationCodeUseCase
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.core.base.classes.BaseViewModelImpl
import com.anvipo.angram.layers.core.events.parameters.ShowAlertMessageEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowErrorEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters.HIDE
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters.SHOW
import com.anvipo.angram.layers.data.gateways.tdLib.errors.TdApiError
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationCodeRouteEventHandler
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.types.EnterAuthenticationCodeScreenSavedInputData
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.types.SetExpectedCodeLengthEventParameters
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.view.EnterAuthenticationCodeFragment.Companion.ENTERED_AUTHENTICATION_CODE
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.view.EnterAuthenticationCodeFragment.Companion.ENTERED_FIRST_NAME
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.view.EnterAuthenticationCodeFragment.Companion.ENTERED_LAST_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EnterAuthenticationCodeViewModelImpl(
    private val routeEventHandler: AuthorizationCoordinatorEnterAuthenticationCodeRouteEventHandler,
    private val useCase: EnterAuthenticationCodeUseCase,
    private val resourceManager: ResourceManager
) : BaseViewModelImpl(), EnterAuthenticationCodeViewModel {

    override val setExpectedCodeLengthEvents: LiveData<SetExpectedCodeLengthEventParameters>
            by lazy { _setExpectedCodeLengthEvents }
    override val showNextButtonEvents: LiveData<ShowViewEventParameters>
            by lazy { _showNextButtonEvents }
    override val showRegistrationViewsEvents: LiveData<ShowViewEventParameters>
            by lazy { _showRegistrationViewsEvents }

    override val enterAuthenticationCodeScreenSavedInputData: LiveData<EnterAuthenticationCodeScreenSavedInputData>
            by lazy { _enterAuthenticationCodeScreenSavedInputData }

    override fun onColdStart() {
        super<BaseViewModelImpl>.onColdStart()
        hideNextButton()
        hideRegistrationViews()
        myLaunch {
            hideProgress()
        }
    }

    override fun onHotStart(savedInstanceState: Bundle) {
        super<BaseViewModelImpl>.onHotStart(savedInstanceState)
        val enteredAuthenticationCode = savedInstanceState.getString(ENTERED_AUTHENTICATION_CODE)
        val enteredFirstName = savedInstanceState.getString(ENTERED_FIRST_NAME)
        val enteredLastName = savedInstanceState.getString(ENTERED_LAST_NAME)
        _enterAuthenticationCodeScreenSavedInputData.value =
            EnterAuthenticationCodeScreenSavedInputData(
                enteredAuthenticationCode = enteredAuthenticationCode,
                enteredFirstName = enteredFirstName,
                enteredLastName = enteredLastName
            )
    }

    override fun onResumeTriggered() {
        if (registrationRequired) {
            showRegistrationViews()
        }

        myLaunch {
            hideProgress()
        }
    }

    override fun onNextButtonPressed(
        enteredAuthenticationCode: CorrectAuthenticationCodeType,
        lastName: String,
        firstName: String
    ) {
        myLaunch {
            showProgress()

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
        myLaunch {
            showProgress()

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
            myLaunch {
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


    private val _enterAuthenticationCodeScreenSavedInputData =
        MutableLiveData<EnterAuthenticationCodeScreenSavedInputData>()

    private val _setExpectedCodeLengthEvents =
        MutableLiveData<SetExpectedCodeLengthEventParameters>()
    private val _showNextButtonEvents =
        MutableLiveData<ShowViewEventParameters>()
    private val _showRegistrationViewsEvents =
        MutableLiveData<ShowViewEventParameters>()

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

        hideProgress()
        showErrorAlert(
            ShowErrorEventParameters(
                text = errorMessage,
                cancelable = true,
                messageDialogTag = null
            )
        )
    }

    private fun setMaxLengthOfEditText(expectedCodeLength: Int) {
        _setExpectedCodeLengthEvents.value = SetExpectedCodeLengthEventParameters(expectedCodeLength)
    }

    private fun showNextButton() {
        _showNextButtonEvents.value = SHOW
    }

    private fun hideNextButton() {
        _showNextButtonEvents.value = HIDE
    }

    private fun showRegistrationViews() {
        _showRegistrationViewsEvents.value = SHOW
    }

    private fun hideRegistrationViews() {
        _showRegistrationViewsEvents.value = HIDE
    }

}