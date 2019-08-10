package com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.enterPasswordScreenFactory

import ru.terrakok.cicerone.android.support.SupportAppScreen

interface EnterAuthenticationPasswordScreenFactory {

    fun createEnterPasswordScreen(): SupportAppScreen

}