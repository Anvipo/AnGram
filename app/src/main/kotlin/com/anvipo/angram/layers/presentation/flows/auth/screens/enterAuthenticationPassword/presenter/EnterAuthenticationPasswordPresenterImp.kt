package com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationPassword.presenter

import com.anvipo.angram.R
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterAuthenticationPassword.EnterAuthenticationPasswordUseCase
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.presentation.common.baseClasses.BasePresenterImp
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationPasswordRouteEventHandler
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationPassword.view.EnterAuthenticationPasswordView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.Dispatchers

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
        viewState.showProgress()

        myLaunch {
            useCase.checkAuthenticationPasswordCatching(enteredAuthenticationPassword)
                .onFailure {
                    val errorMessage: String = resourceManager.run {
                        getString(R.string.unknown_error)
                    }

                    myLaunch(Dispatchers.Main) {
                        viewState.hideProgress()
                        viewState.showErrorAlert(errorMessage)
                    }
                }
        }
    }

    override fun onBackPressed() {
        routeEventHandler.onPressedBackButtonInEnterAuthenticationPasswordScreen()
    }

}