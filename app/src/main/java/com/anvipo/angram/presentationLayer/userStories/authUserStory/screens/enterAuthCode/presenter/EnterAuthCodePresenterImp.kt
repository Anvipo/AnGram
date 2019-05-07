package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.presenter

import com.anvipo.angram.businessLogicLayer.useCases.enterAuthCodeUseCase.EnterAuthCodeUseCase
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view.EnterAuthCodeView
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

class EnterAuthCodePresenterImp(
    private val view: WeakReference<EnterAuthCodeView>,
    private val useCase: EnterAuthCodeUseCase
) : EnterAuthCodePresenter {
    override val job: Job
        get() = TODO("not implemented")
    override val coroutineExceptionHandler: CoroutineExceptionHandler
        get() = TODO("not implemented")

    override fun coldStart() {
        TODO("not implemented")
    }

    override fun onBackPressed() {
        TODO("not implemented")
    }

    override fun onDestroy() {
        TODO("not implemented")
    }

    override val coroutineContext: CoroutineContext
        get() = TODO("not implemented")
}