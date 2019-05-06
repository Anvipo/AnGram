package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter

import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.errors.TdApiError
import com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase.EnterPhoneNumberUseCase
import com.anvipo.angram.coreLayer.message.ISentDataNotifier
import com.anvipo.angram.presentationLayer.common.baseClasses.BasePresenter
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@InjectViewState
class EnterPhoneNumberPresenterImp(
    private val useCase: EnterPhoneNumberUseCase,
    private val onEnteredCorrectPhoneNumberNotifier: ISentDataNotifier<String>,
    private val onBackButtonPressedNotifier: ISentDataNotifier<Unit>
) : BasePresenter<EnterPhoneNumberView>(), EnterPhoneNumberPresenter {

    override fun didTapNextButton(enteredPhoneNumber: String) {
        GlobalScope.launch {
            useCase.setAuthenticationPhoneNumberCatching(enteredPhoneNumber)
                .onSuccess {
                    onEnteredCorrectPhoneNumberNotifier.send(enteredPhoneNumber)
                }
                .onFailure { error ->
                    // TODO: translate strings
                    val errorMessage: String = when (error) {
                        is TdApiError.Custom.EmptyPhoneNumber -> error.localizedMessage
                        is TdApiError.Custom.PhoneNumberInvalid -> "Phone number invalid"
                        else -> "Unknown error"
                    }

                    withContext(Dispatchers.Main) {
                        viewState.showErrorAlert(errorMessage)
                    }
                }
        }
    }

    override fun onBackPressed() {
        onBackButtonPressedNotifier.send(Unit)
    }

}