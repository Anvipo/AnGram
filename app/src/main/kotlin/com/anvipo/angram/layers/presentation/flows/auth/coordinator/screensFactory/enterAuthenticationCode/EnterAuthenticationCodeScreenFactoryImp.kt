package com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.enterAuthenticationCode

import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule.enterAuthenticationCodeScreenQualifier
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.terrakok.cicerone.android.support.SupportAppScreen

class EnterAuthenticationCodeScreenFactoryImp(
    private val koinScope: Scope
) : EnterAuthenticationCodeScreenFactory {

    override fun createEnterAuthenticationCodeScreen(
        parameters: EnterAuthenticationCodeModule.EnterAuthenticationCodeScreenParameters
    ): SupportAppScreen =
        koinScope.get(enterAuthenticationCodeScreenQualifier) {
            parametersOf(parameters)
        }

}