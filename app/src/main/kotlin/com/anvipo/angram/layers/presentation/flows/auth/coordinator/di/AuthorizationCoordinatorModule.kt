package com.anvipo.angram.layers.presentation.flows.auth.coordinator.di

import com.anvipo.angram.layers.application.di.LaunchSystemModule.systemMessageSendChannelQualifier
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.routerQualifier
import com.anvipo.angram.layers.data.di.GatewaysModule.authorizationTDLibGatewayQualifier
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.AuthorizationCoordinatorImp
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.interfaces.AuthorizationCoordinator
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.authorization.AuthorizationScreensFactory
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.authorization.AuthorizationScreensFactoryImp
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.di.AddProxyModule.addProxyScreenFactoryQualifier
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule.enterAuthenticationCodeScreenFactoryQualifier
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationPassword.di.EnterAuthenticationPasswordModule.enterAuthenticationPasswordScreenFactoryQualifier
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterPhoneNumber.di.EnterPhoneNumberModule.enterPhoneNumberScreenFactoryQualifier
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object AuthorizationCoordinatorModule {

    val authorizationCoordinatorQualifier: StringQualifier = named("authorizationCoordinator")
    private val authorizationScreensFactoryQualifier: StringQualifier = named("authorizationScreensFactory")

    val module: Module = module {

        single<AuthorizationCoordinator>(authorizationCoordinatorQualifier) {
            AuthorizationCoordinatorImp(
                router = get(routerQualifier),
                screensFactory = get(authorizationScreensFactoryQualifier),
                tdLibGateway = get(authorizationTDLibGatewayQualifier),
                systemMessageSendChannel = get(systemMessageSendChannelQualifier)
            )
        }

        single<AuthorizationScreensFactory>(authorizationScreensFactoryQualifier) {
            AuthorizationScreensFactoryImp(
                enterPhoneNumberScreenFactory = get(enterPhoneNumberScreenFactoryQualifier),
                enterAuthenticationPasswordScreenFactory = get(enterAuthenticationPasswordScreenFactoryQualifier),
                enterAuthenticationCodeScreenFactory = get(enterAuthenticationCodeScreenFactoryQualifier),
                addProxyScreenFactory = get(addProxyScreenFactoryQualifier)
            )
        }

    }

}