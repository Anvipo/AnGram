package com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.enterAuthenticationCode

import ru.terrakok.cicerone.android.support.SupportAppScreen

interface EnterAuthenticationCodeScreenFactory {

    fun createEnterAuthCodeScreen(
        expectedCodeLength: Int,
        enteredPhoneNumber: String,
        registrationRequired: Boolean,
        termsOfServiceText: String,
        shouldShowBackButton: Boolean
    ): SupportAppScreen

}