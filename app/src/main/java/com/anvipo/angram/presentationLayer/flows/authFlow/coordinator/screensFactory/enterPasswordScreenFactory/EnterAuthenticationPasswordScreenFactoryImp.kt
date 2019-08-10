package com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.enterPasswordScreenFactory

import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationPassword.di.EnterAuthenticationPasswordModule.enterAuthenticationPasswordScreenQualifier
import org.koin.core.scope.Scope
import ru.terrakok.cicerone.android.support.SupportAppScreen

class EnterAuthenticationPasswordScreenFactoryImp(
    private val koinScope: Scope
) : EnterAuthenticationPasswordScreenFactory {

    override fun createEnterPasswordScreen(): SupportAppScreen =
        koinScope.get(enterAuthenticationPasswordScreenQualifier)

}