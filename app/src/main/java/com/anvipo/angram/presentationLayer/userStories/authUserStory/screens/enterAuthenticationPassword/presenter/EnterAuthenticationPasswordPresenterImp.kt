package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationPassword.presenter

import com.anvipo.angram.BuildConfig
import com.anvipo.angram.R
import com.anvipo.angram.businessLogicLayer.useCases.authUserStory.enterAuthenticationPasswordUseCase.EnterAuthenticationPasswordUseCase
import com.anvipo.angram.coreLayer.CoreHelpers
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.presentationLayer.common.baseClasses.BasePresenterImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationPasswordRouteEventHandler
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationPassword.view.EnterAuthenticationPasswordView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@InjectViewState
class EnterAuthenticationPasswordPresenterImp(
    private val routeEventHandler: AuthorizationCoordinatorEnterAuthenticationPasswordRouteEventHandler,
    private val useCase: EnterAuthenticationPasswordUseCase,
    private val resourceManager: ResourceManager
) : BasePresenterImp<EnterAuthenticationPasswordView>(), EnterAuthenticationPasswordPresenter {

    override fun onResumeTriggered() {
        viewState.hideProgress()
    }

    override fun onNextButtonPressed(enteredAuthenticationPassword: CorrectAuthenticationPasswordType) {
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
            useCase.checkAuthenticationPasswordCatching(enteredAuthenticationPassword)
                .onSuccess {
                    routeEventHandler.onEnterCorrectAuthenticationPassword()
                }
                .onFailure { error ->
                    val errorMessage: String = resourceManager.run {
                        when (error) {
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

    override fun onBackPressed() {
        routeEventHandler.onPressedBackButtonInEnterAuthenticationPasswordScreen()
    }

    override fun cancelAllJobs() {
        onNextButtonPressedJob?.cancel()
    }

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    private var onNextButtonPressedJob: Job? = null

}