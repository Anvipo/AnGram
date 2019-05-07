package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter

import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.errors.TdApiError
import com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase.EnterPhoneNumberUseCase
import com.anvipo.angram.presentationLayer.common.baseClasses.BasePresenterImp
import com.anvipo.angram.presentationLayer.common.interfaces.Coordinatorable
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlin.coroutines.CoroutineContext

@InjectViewState
class EnterPhoneNumberPresenterImp(
    override val useCase: EnterPhoneNumberUseCase,
    private val enteredCorrectPhoneNumberSendChannel: SendChannel<String>,
    private val backButtonPressedInPhoneNumberScreenSendChannel: SendChannel<Unit>
) : BasePresenterImp<EnterPhoneNumberView>(), EnterPhoneNumberPresenter {

    override val coordinator: Coordinatorable
        get() = TODO("not implemented")
    override val job: Job
        get() = TODO("not implemented")
    override val coroutineExceptionHandler: CoroutineExceptionHandler
        get() = TODO("not implemented")

    override fun coldStart() {
        TODO("not implemented")
    }

    override val coroutineContext: CoroutineContext
        get() = TODO("not implemented")

    override fun onNextButtonPressed(enteredPhoneNumber: String) {
        GlobalScope.launch {
            useCase.setAuthenticationPhoneNumberCatching(enteredPhoneNumber)
                .onSuccess {
                    enteredCorrectPhoneNumberSendChannel.offer(enteredPhoneNumber)
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
        backButtonPressedInPhoneNumberScreenSendChannel.offer(Unit)
    }

}
