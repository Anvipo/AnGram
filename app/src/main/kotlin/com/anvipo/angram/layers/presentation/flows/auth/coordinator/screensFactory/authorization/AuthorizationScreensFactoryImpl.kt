package com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.authorization

import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.addProxy.AddProxyScreenFactory
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.enterAuthenticationCode.EnterAuthenticationCodeScreenFactory
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.enterAuthenticationPassword.EnterAuthenticationPasswordScreenFactory
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.enterPhoneNumber.EnterPhoneNumberScreenFactory

class AuthorizationScreensFactoryImpl(
    override val enterPhoneNumberScreenFactory: EnterPhoneNumberScreenFactory,
    override val enterAuthenticationCodeScreenFactory: EnterAuthenticationCodeScreenFactory,
    override val enterAuthenticationPasswordScreenFactory: EnterAuthenticationPasswordScreenFactory,
    override val addProxyScreenFactory: AddProxyScreenFactory
) : AuthorizationScreensFactory
