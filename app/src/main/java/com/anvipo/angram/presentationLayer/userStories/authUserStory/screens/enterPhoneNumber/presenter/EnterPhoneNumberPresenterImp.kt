package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter

import com.anvipo.angram.BuildConfig
import com.anvipo.angram.R
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.errors.TdApiError
import com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase.EnterPhoneNumberUseCase
import com.anvipo.angram.coreLayer.CoreHelpers.debugLog
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.presentationLayer.common.baseClasses.BasePresenterImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.AuthorizationCoordinatorOutput
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@InjectViewState
class EnterPhoneNumberPresenterImp(
    override val useCase: EnterPhoneNumberUseCase,
    override val coordinator: AuthorizationCoordinatorOutput,
    private val resourceManager: ResourceManager
) : BasePresenterImp<EnterPhoneNumberView>(), EnterPhoneNumberPresenter {

    override fun coldStart() {
        viewState.hideNextButton()
    }

    override fun onNextButtonPressed(enteredPhoneNumber: String) {
        val onNextButtonPressedCoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            if (BuildConfig.DEBUG) {
                val text = throwable.localizedMessage
                debugLog(text)
                viewState.showErrorAlert(text)
            }
        }

        viewState.showProgress()

        onNextButtonPressedJob = launch(
            context = coroutineContext + onNextButtonPressedCoroutineExceptionHandler
        ) {
            useCase.setAuthenticationPhoneNumberCatching(enteredPhoneNumber)
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        viewState.hideProgress()
                    }

                    coordinator.onEnterCorrectPhoneNumber(enteredPhoneNumber)
                }
                .onFailure { error ->
                    val errorMessage: String = resourceManager.run {
                        when (error) {
                            is TdApiError.Custom.EmptyPhoneNumber -> getString(R.string.phone_number_cant_be_empty)
                            is TdApiError.Custom.PhoneNumberInvalid -> getString(R.string.phone_number_invalid)
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

        // TODO: phone number length
        if (text.length < 12) {
            viewState.hideNextButton()
            return
        }

        viewState.showNextButton()
    }

    override fun onBackPressed() {
        coordinator.onPressedBackButtonInEnterPhoneNumberScreen()
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

}
