package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.viewModel

import com.anvipo.angram.layers.presentation.flows.authorization.screens.base.viewModel.BaseAuthorizationFlowViewModel
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType

interface EnterAuthenticationPasswordViewModel : BaseAuthorizationFlowViewModel {

    fun onNextButtonPressed(enteredAuthenticationPassword: CorrectAuthenticationPasswordType)
    fun onAuthenticationPasswordTextChanged(newText: String)

}