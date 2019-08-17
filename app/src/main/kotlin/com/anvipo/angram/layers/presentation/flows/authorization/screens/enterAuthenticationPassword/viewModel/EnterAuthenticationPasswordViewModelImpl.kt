package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.viewModel

import com.anvipo.angram.R
import com.anvipo.angram.layers.businessLogic.useCases.flows.authorization.enterAuthenticationPassword.EnterAuthenticationPasswordUseCase
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.core.base.classes.BaseViewModelImpl
import com.anvipo.angram.layers.core.events.parameters.ShowErrorEventParameters
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationPasswordRouteEventHandler
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EnterAuthenticationPasswordViewModelImpl(
    private val routeEventHandler: AuthorizationCoordinatorEnterAuthenticationPasswordRouteEventHandler,
    private val useCase: EnterAuthenticationPasswordUseCase,
    private val resourceManager: ResourceManager
) : BaseViewModelImpl(), EnterAuthenticationPasswordViewModel {

    override fun onResumeTriggered() {
        hideProgress()
    }

    override fun onNextButtonPressed(enteredAuthenticationPassword: CorrectAuthenticationPasswordType) {
        showProgress()

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
                        hideProgress()
                        showErrorAlert(
                            ShowErrorEventParameters(
                                text = errorMessage,
                                cancelable = true,
                                messageDialogTag = null
                            )
                        )
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