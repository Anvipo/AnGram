package com.anvipo.angram.presentationLayer.flows.auth.screens.enterAuthenticationPassword.presenter

import com.anvipo.angram.presentationLayer.common.interfaces.BasePresenter
import com.anvipo.angram.presentationLayer.flows.auth.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType

interface EnterAuthenticationPasswordPresenter : BasePresenter {

    fun onNextButtonPressed(enteredAuthenticationPassword: CorrectAuthenticationPasswordType)

}