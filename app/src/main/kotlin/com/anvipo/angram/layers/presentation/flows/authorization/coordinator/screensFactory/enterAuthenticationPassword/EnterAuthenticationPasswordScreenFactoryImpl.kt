package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationPassword

import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.view.EnterPasswordScreen
import org.koin.core.scope.Scope

class EnterAuthenticationPasswordScreenFactoryImpl(
    private val koinScope: Scope
) : EnterAuthenticationPasswordScreenFactory {

    override fun createEnterPasswordScreen(): EnterPasswordScreen = koinScope.get()

}