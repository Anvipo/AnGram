package com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.enterAuthenticationCode

import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule
import ru.terrakok.cicerone.android.support.SupportAppScreen

interface EnterAuthenticationCodeScreenFactory {

    fun createEnterAuthenticationCodeScreen(
        parameters: EnterAuthenticationCodeModule.EnterAuthenticationCodeScreenParameters
    ): SupportAppScreen

}