package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationPhoneNumber

import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.view.navigation.EnterAuthenticationPhoneNumberScreen

object EnterAuthenticationPhoneNumberScreenFactoryImpl :
    EnterAuthenticationPhoneNumberScreenFactory {

    override fun createEnterAuthenticationPhoneNumberScreen(): EnterAuthenticationPhoneNumberScreen =
        authorizationCoordinatorScope!!.get()

}