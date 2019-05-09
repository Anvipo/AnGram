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
    }

    override fun onNextButtonPressed(enteredAuthCode: CorrectAuthCodeType) {
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
            useCase.checkAuthenticationCodeCatching(enteredAuthCode)
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        viewState.hideProgress()
                    }

                    coordinator.onEnterCorrectAuthCode(enteredAuthCode)
                }
                .onFailure { error ->
                    if (error.message == "PHONE_NUMBER_UNOCCUPIED") {
                        coordinator.onEnterCorrectAuthCode(enteredAuthCode)
                        return@launch
                    }

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
        viewState.setMaxLengthOfEditText(this.expectedCodeLength)
    }

    override fun onGetEnteredPhoneNumber(enteredPhoneNumber: String) {
        this.enteredPhoneNumber = enteredPhoneNumber
    }

    override fun onAuthCodeTextChanged(text: CharSequence?) {
        if (text == null) {
            viewState.hideNextButton()
            return
        }

        if (text.length.toUInt() < expectedCodeLength) {
            viewState.hideNextButton()
            return
        }

        viewState.showNextButton()
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

}