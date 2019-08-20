package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationPassword

import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.view.EnterPasswordScreen

interface EnterAuthenticationPasswordScreenFactory {

    fun createEnterPasswordScreen(): EnterPasswordScreen

}