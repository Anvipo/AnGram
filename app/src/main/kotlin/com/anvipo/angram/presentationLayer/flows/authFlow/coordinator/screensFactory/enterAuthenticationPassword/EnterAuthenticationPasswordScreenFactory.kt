package com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.enterAuthenticationPassword

import ru.terrakok.cicerone.android.support.SupportAppScreen

interface EnterAuthenticationPasswordScreenFactory {

    fun createEnterPasswordScreen(): SupportAppScreen

}