package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationCode

import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.view.navigation.EnterAuthenticationCodeScreen
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.view.navigation.EnterAuthenticationCodeScreenParameters
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope

class EnterAuthenticationCodeScreenFactoryImpl(
    private val koinScope: Scope
) : EnterAuthenticationCodeScreenFactory {

    override fun createEnterAuthenticationCodeScreen(
        parameters: EnterAuthenticationCodeScreenParameters
    ): EnterAuthenticationCodeScreen = koinScope.get { parametersOf(parameters) }

}