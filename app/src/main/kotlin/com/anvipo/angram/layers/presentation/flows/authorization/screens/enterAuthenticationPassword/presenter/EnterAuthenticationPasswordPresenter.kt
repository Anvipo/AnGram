package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.presenter

import com.anvipo.angram.layers.core.base.interfaces.BasePresenter
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType

interface EnterAuthenticationPasswordPresenter : BasePresenter {

    fun onNextButtonPressed(enteredAuthenticationPassword: CorrectAuthenticationPasswordType)

}