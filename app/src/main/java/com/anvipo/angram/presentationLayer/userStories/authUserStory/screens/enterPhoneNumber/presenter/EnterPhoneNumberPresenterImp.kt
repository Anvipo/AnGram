package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter

import com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase.EnterPhoneNumberUseCase
import com.anvipo.angram.global.tdApi.TdApiError
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

class EnterPhoneNumberPresenterImp(
    private val view: WeakReference<EnterPhoneNumberView>,
    private val useCase: EnterPhoneNumberUseCase
) : EnterPhoneNumberPresenter {

    override fun didTapNextButton(enteredPhoneNumber: String) {
        GlobalScope.launch {
            useCase.setAuthenticationPhoneNumberCatching(enteredPhoneNumber)
                .onSuccess {
                    view.get()?.onEnteredCorrectPhoneNumber?.invoke(enteredPhoneNumber)
                }
                .onFailure { error ->
                    // TODO: translate strings
                    val errorMessage: String = when (error) {
                        is TdApiError.Custom.EmptyPhoneNumber -> error.localizedMessage
                        is TdApiError.Custom.PhoneNumberInvalid -> "Phone number invalid"
                        else -> "Unknown error"
                    }

                    withContext(Dispatchers.Main) {
                        view.get()?.showErrorAlert(errorMessage)
                    }
                }
        }
    }

}