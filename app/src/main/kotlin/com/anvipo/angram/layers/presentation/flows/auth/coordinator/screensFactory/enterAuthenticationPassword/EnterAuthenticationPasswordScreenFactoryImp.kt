package com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.enterAuthenticationPassword

import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationPassword.di.EnterAuthenticationPasswordModule.enterAuthenticationPasswordScreenQualifier
import org.koin.core.scope.Scope
import ru.terrakok.cicerone.android.support.SupportAppScreen

class EnterAuthenticationPasswordScreenFactoryImp(
    private val koinScope: Scope
) : EnterAuthenticationPasswordScreenFactory {

    override fun createEnterPasswordScreen(): SupportAppScreen =
        koinScope.get(enterAuthenticationPasswordScreenQualifier)

}