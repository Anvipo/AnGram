package com.anvipo.angram.presentationLayer.flows.auth.coordinator.screensFactory.authorization

import com.anvipo.angram.presentationLayer.flows.auth.coordinator.screensFactory.addProxy.AddProxyScreenFactory
import com.anvipo.angram.presentationLayer.flows.auth.coordinator.screensFactory.enterAuthenticationCode.EnterAuthenticationCodeScreenFactory
import com.anvipo.angram.presentationLayer.flows.auth.coordinator.screensFactory.enterAuthenticationPassword.EnterAuthenticationPasswordScreenFactory
import com.anvipo.angram.presentationLayer.flows.auth.coordinator.screensFactory.enterPhoneNumber.EnterPhoneNumberScreenFactory

interface AuthorizationScreensFactory {

    val enterPhoneNumberScreenFactory: EnterPhoneNumberScreenFactory

    val enterAuthenticationCodeScreenFactory: EnterAuthenticationCodeScreenFactory

    val enterAuthenticationPasswordScreenFactory: EnterAuthenticationPasswordScreenFactory

    val addProxyScreenFactory: AddProxyScreenFactory

}