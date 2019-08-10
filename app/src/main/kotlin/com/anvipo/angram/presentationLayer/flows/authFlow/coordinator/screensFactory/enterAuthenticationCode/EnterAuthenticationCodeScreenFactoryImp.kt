package com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.enterAuthenticationCode

import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule.enterAuthenticationCodeScreenQualifier
import org.koin.core.scope.Scope
import ru.terrakok.cicerone.android.support.SupportAppScreen

@Suppress("RemoveExplicitTypeArguments")
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