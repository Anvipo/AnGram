package com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationPassword.presenter

import com.anvipo.angram.R
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterAuthenticationPassword.EnterAuthenticationPasswordUseCase
import com.anvipo.angram.layers.core.CoroutineExceptionHandlerWithLogger
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.presentation.common.baseClasses.BasePresenterImp
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationPasswordRouteEventHandler
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationPassword.view.EnterAuthenticationPasswordView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        val onNextButtonPressedCEH = CoroutineExceptionHandlerWithLogger { _, error ->
            viewState.showErrorAlert(error.localizedMessage)
        }

        viewState.showProgress()

        launch(
            context = coroutineContext + onNextButtonPressedCEH
        ) {
            useCase.checkAuthenticationPasswordCatching(enteredAuthenticationPassword)
                .onSuccess {
                    routeEventHandler.onEnterCorrectAuthenticationPassword()
                }
                .onFailure {
                    val errorMessage: String = resourceManager.run {
                        getString(R.string.unknown_error)
                    }

                    withContext(Dispatchers.Main) {
                        viewState.hideProgress()
                        viewState.showErrorAlert(errorMessage)
                    }
                }
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    override fun onBackPressed() {
        routeEventHandler.onPressedBackButtonInEnterAuthenticationPasswordScreen()
    }

    override val coroutineContext: CoroutineContext = Dispatchers.IO

}