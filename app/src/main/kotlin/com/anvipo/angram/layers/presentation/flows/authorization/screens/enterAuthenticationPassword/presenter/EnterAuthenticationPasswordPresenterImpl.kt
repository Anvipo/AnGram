package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.presenter

import com.anvipo.angram.R
import com.anvipo.angram.layers.businessLogic.useCases.authFlow.enterAuthenticationPassword.EnterAuthenticationPasswordUseCase
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.core.base.classes.BasePresenterImpl
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationPasswordRouteEventHandler
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.view.EnterAuthenticationPasswordView
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@InjectViewState
class EnterAuthenticationPasswordPresenterImpl(
    private val routeEventHandler: AuthorizationCoordinatorEnterAuthenticationPasswordRouteEventHandler,
    private val useCase: EnterAuthenticationPasswordUseCase,
    private val resourceManager: ResourceManager
) : BasePresenterImpl<EnterAuthenticationPasswordView>(), EnterAuthenticationPasswordPresenter {

    override fun onResumeTriggered() {
        viewState.hideProgress()
    }

    override fun onNextButtonPressed(enteredAuthenticationPassword: CorrectAuthenticationPasswordType) {
        viewState.showProgress()

        myLaunch {
            val checkAuthenticationPasswordResult = withContext(Dispatchers.IO) {
                useCase.checkAuthenticationPasswordCatching(enteredAuthenticationPassword)
            }

            checkAuthenticationPasswordResult
                .onFailure {
                    val errorMessage: String = resourceManager.run {
                        getString(R.string.unknown_error)
                    }

                    withContext(Dispatchers.Main) {
                        viewState.hideProgress()
                        viewState.showErrorAlert(errorMessage)
                    }
                }
        }
    }

    override fun onBackPressed() {
        myLaunch {
            routeEventHandler.onPressedBackButtonInEnterAuthenticationPasswordScreen()
        }
    }

}