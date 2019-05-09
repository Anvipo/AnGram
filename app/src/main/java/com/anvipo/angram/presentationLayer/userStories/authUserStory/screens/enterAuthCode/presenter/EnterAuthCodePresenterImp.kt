package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.presenter

import com.anvipo.angram.businessLogicLayer.useCases.enterAuthCodeUseCase.EnterAuthCodeUseCase
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.presentationLayer.common.baseClasses.BasePresenterImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorEnterAuthCodeOutput
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.types.CorrectAuthCodeType
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view.EnterAuthCodeView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@InjectViewState
class EnterAuthCodePresenterImp(
    override val coordinator: AuthorizationCoordinatorEnterAuthCodeOutput,
    override val useCase: EnterAuthCodeUseCase,
    private val resourceManager: ResourceManager
) : BasePresenterImp<EnterAuthCodeView>(), EnterAuthCodePresenter {

    override fun onNextButtonPressed(enteredPhoneNumber: CorrectAuthCodeType) {
        TODO("not implemented")
    }

    override fun onAuthCodeTextChanged(text: CharSequence?) {
        TODO("not implemented")
    }

    override fun onBackPressed() {
        coordinator.onPressedBackButtonInEnterAuthCodeScreen()
    }

    override fun cancelAllJobs() {
    }

    override val coroutineContext: CoroutineContext = Dispatchers.IO

}