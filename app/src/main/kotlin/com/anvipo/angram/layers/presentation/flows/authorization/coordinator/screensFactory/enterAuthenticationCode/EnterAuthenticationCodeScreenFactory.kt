package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationCode

import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.view.navigation.EnterAuthenticationCodeScreen
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.view.navigation.EnterAuthenticationCodeScreenParameters

interface EnterAuthenticationCodeScreenFactory {

    fun createEnterAuthenticationCodeScreen(
        parameters: EnterAuthenticationCodeScreenParameters
    ): EnterAuthenticationCodeScreen

}