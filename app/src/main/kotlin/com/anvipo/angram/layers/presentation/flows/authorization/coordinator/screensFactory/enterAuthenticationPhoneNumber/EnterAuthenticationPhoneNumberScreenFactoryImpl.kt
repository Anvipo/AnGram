package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationPhoneNumber

import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.di.EnterAuthenticationPhoneNumberModule.enterAuthenticationPhoneNumberScreenQualifier
import org.koin.core.scope.Scope
import ru.terrakok.cicerone.android.support.SupportAppScreen

class EnterAuthenticationPhoneNumberScreenFactoryImpl(
    private val koinScope: Scope
) : EnterAuthenticationPhoneNumberScreenFactory {

    override fun createEnterAuthenticationPhoneNumberScreen(): SupportAppScreen =
        koinScope.get(enterAuthenticationPhoneNumberScreenQualifier)

}