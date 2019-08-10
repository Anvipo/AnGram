package com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.enterAuthenticationCode

import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule.enterAuthenticationCodeScreenQualifier
import org.koin.core.scope.Scope
import ru.terrakok.cicerone.android.support.SupportAppScreen

class EnterAuthenticationCodeScreenFactoryImp(
    private val koinScope: Scope
) : EnterAuthenticationCodeScreenFactory {

    override fun createEnterAuthCodeScreen(
        expectedCodeLength: Int,
        enteredPhoneNumber: String,
        registrationRequired: Boolean,
        termsOfServiceText: String,
        shouldShowBackButton: Boolean
    ): SupportAppScreen =
        koinScope.get(enterAuthenticationCodeScreenQualifier)

}