package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.presenter

import com.anvipo.angram.BuildConfig
import com.anvipo.angram.R
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.errors.TdApiError
import com.anvipo.angram.businessLogicLayer.useCases.enterAuthCodeUseCase.EnterAuthCodeUseCase
import com.anvipo.angram.coreLayer.CoreHelpers
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.presentationLayer.common.baseClasses.BasePresenterImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorEnterAuthCodeOutput
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.types.CorrectAuthCodeType
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view.EnterAuthCodeView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@InjectViewState
class EnterAuthCodePresenterImp(
    override val coordinator: AuthorizationCoordinatorEnterAuthCodeOutput,
    override val useCase: EnterAuthCodeUseCase,
    private val resourceManager: ResourceManager
) : BasePresenterImp<EnterAuthCodeView>(), EnterAuthCodePresenter {

    override fun coldStart() {
        viewState.hideNextButton()
        viewState.hideRegistrationViews()
        viewState.showProgress()
    }

    override fun onStartTriggered() {
        viewState.hideProgress()
        if (registrationRequired) {
            viewState.showRegistrationViews()
        }
    }

    override fun onNextButtonPressed(
        enteredAuthCode: CorrectAuthCodeType,
        lastName: String,
        firstName: String
    ) {
        val onNextButtonPressedCoroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
            if (BuildConfig.DEBUG) {
                val errorText = error.localizedMessage
                CoreHelpers.debugLog(errorText)
                viewState.showErrorAlert(errorText)
            }
        }

        viewState.showProgress()

        onNextButtonPressedJob = launch(
            context = coroutineContext + onNextButtonPressedCoroutineExceptionHandler
        ) {
            useCase.checkAuthenticationCodeCatching(
                enteredAuthCode = enteredAuthCode,
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
                            is TdApiError.Custom.BadRequest -> getString(R.string.unknown_error)
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
        viewState.showAlertMessage(termsOfServiceText)
    }

    override fun onAuthCodeTextChanged(text: CharSequence?) {
        if (text.isNullOrBlank()) {
            authCodeIsEnteredAndCorrect = false
            viewState.hideNextButton()
            return
        }

        if (text.length.toUInt() < expectedCodeLength) {
            authCodeIsEnteredAndCorrect = false
            viewState.hideNextButton()
            return
        }

        authCodeIsEnteredAndCorrect = true

        if (authCodeIsEnteredAndCorrect) {
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

        if (authCodeIsEnteredAndCorrect) {
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

        if (authCodeIsEnteredAndCorrect) {
            if (registrationRequired && firstNameIsEntered && lastNameIsEntered) {
                viewState.showNextButton()
            } else {
                viewState.hideNextButton()
            }
        }
    }

    override fun onBackPressed() {
        coordinator.onPressedBackButtonInEnterAuthCodeScreen()
    }

    override fun cancelAllJobs() {
        onNextButtonPressedJob?.cancel()
    }

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    private var onNextButtonPressedJob: Job? = null
    private var enteredPhoneNumber: String = ""
    private var expectedCodeLength: UInt = 0u

    private var authCodeIsEnteredAndCorrect: Boolean = false

    private var registrationRequired: Boolean = false

    private var firstNameIsEntered: Boolean = false
    private var lastNameIsEntered: Boolean = false

}