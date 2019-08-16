package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationCode

import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule
import ru.terrakok.cicerone.android.support.SupportAppScreen

interface EnterAuthenticationCodeScreenFactory {

    fun createEnterAuthenticationCodeScreen(
        parameters: EnterAuthenticationCodeModule.EnterAuthenticationCodeScreenParameters
    ): SupportAppScreen

}