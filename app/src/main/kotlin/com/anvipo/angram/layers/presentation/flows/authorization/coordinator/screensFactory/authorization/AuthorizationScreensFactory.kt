package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.authorization

import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.addProxy.AddProxyScreenFactory
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationCode.EnterAuthenticationCodeScreenFactory
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationPassword.EnterAuthenticationPasswordScreenFactory
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterAuthenticationPhoneNumber.EnterAuthenticationPhoneNumberScreenFactory

interface AuthorizationScreensFactory {

    val enterAuthenticationPhoneNumberScreenFactory: EnterAuthenticationPhoneNumberScreenFactory

    val enterAuthenticationCodeScreenFactory: EnterAuthenticationCodeScreenFactory

    val enterAuthenticationPasswordScreenFactory: EnterAuthenticationPasswordScreenFactory

    val addProxyScreenFactory: AddProxyScreenFactory

}