package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter

import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.errors.TdApiError
import com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase.EnterPhoneNumberUseCase
import com.anvipo.angram.presentationLayer.common.baseClasses.BasePresenterImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.AuthorizationCoordinatorOutput
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@InjectViewState
class EnterPhoneNumberPresenterImp(
    override val useCase: EnterPhoneNumberUseCase,
    override val coordinator: AuthorizationCoordinatorOutput
) : BasePresenterImp<EnterPhoneNumberView>(), EnterPhoneNumberPresenter {

    override fun coldStart() {
        TODO("not implemented")
    }

    override fun onNextButtonPressed(enteredPhoneNumber: String) {
        val onNextButtonPressedCoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            print("")
        }

        viewState.showLoading()

        onNextButtonPressedJob = launch(context = coroutineContext + onNextButtonPressedCoroutineExceptionHandler) {
            useCase.setAuthenticationPhoneNumberCatching(enteredPhoneNumber)
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        viewState.hideLoading()
                    }

                    coordinator.onEnterCorrectPhoneNumber(enteredPhoneNumber)
                }
                .onFailure { error ->
                    // TODO: translate strings
                    val errorMessage: String = when (error) {
                        is TdApiError.Custom.EmptyPhoneNumber -> error.localizedMessage
                        is TdApiError.Custom.PhoneNumberInvalid -> "Phone number invalid"
                        else -> "Unknown error"
                    }

                    withContext(Dispatchers.Main) {
                        viewState.hideLoading()
                        viewState.showErrorAlert(errorMessage)
                    }
                }
        }
    }

    override fun onBackPressed() {
        coordinator.onPressedBackButtonInEnterPhoneNumberScreen()
    }


    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override fun cancelAllJobs() {
        onNextButtonPressedJob?.cancel()
    }

    private var onNextButtonPressedJob: Job? = null

}
