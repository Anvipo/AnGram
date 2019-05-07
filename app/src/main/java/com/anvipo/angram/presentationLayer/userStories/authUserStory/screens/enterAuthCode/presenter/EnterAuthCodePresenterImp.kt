package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.presenter

import com.anvipo.angram.businessLogicLayer.useCases.enterAuthCodeUseCase.EnterAuthCodeUseCase
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view.EnterAuthCodeView
import java.lang.ref.WeakReference

class EnterAuthCodePresenterImp(
    private val view: WeakReference<EnterAuthCodeView>,
    private val useCase: EnterAuthCodeUseCase
) : EnterAuthCodePresenter {

    override fun coldStart() {
        TODO("not implemented")
    }

    override fun onBackPressed() {
        TODO("not implemented")
    }

}