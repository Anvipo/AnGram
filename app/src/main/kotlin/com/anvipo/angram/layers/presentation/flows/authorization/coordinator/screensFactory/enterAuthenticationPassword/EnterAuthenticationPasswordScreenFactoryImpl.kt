package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationPassword

import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.di.EnterAuthenticationPasswordModule.enterAuthenticationPasswordScreenQualifier
import org.koin.core.scope.Scope
import ru.terrakok.cicerone.android.support.SupportAppScreen

class EnterAuthenticationPasswordScreenFactoryImpl(
    private val koinScope: Scope
) : EnterAuthenticationPasswordScreenFactory {

    override fun createEnterPasswordScreen(): SupportAppScreen =
        koinScope.get(enterAuthenticationPasswordScreenQualifier)

}