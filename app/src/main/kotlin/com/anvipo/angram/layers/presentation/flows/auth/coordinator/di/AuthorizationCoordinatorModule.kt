package com.anvipo.angram.layers.presentation.flows.auth.coordinator.di

import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.addProxy.AddProxyScreenFactory
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.authorization.AuthorizationScreensFactory
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.authorization.AuthorizationScreensFactoryImp
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.enterAuthenticationCode.EnterAuthenticationCodeScreenFactory
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.enterAuthenticationPassword.EnterAuthenticationPasswordScreenFactory
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.enterPhoneNumber.EnterPhoneNumberScreenFactory
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.di.AddProxyModule.addProxyScreenFactoryQualifier
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule.enterAuthenticationCodeScreenFactoryQualifier
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationPassword.di.EnterAuthenticationPasswordModule.enterAuthenticationPasswordScreenFactoryQualifier
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterPhoneNumber.di.EnterPhoneNumberModule.enterPhoneNumberScreenFactoryQualifier
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object AuthorizationCoordinatorModule {

    val authorizationScreensFactoryQualifier: StringQualifier = named("authorizationScreensFactory")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        factory<AuthorizationScreensFactory>(authorizationScreensFactoryQualifier) {
            AuthorizationScreensFactoryImp(
                enterPhoneNumberScreenFactory = get<EnterPhoneNumberScreenFactory>(
                    enterPhoneNumberScreenFactoryQualifier
                ),
                enterAuthenticationPasswordScreenFactory = get<EnterAuthenticationPasswordScreenFactory>(
                    enterAuthenticationPasswordScreenFactoryQualifier
                ),
                enterAuthenticationCodeScreenFactory = get<EnterAuthenticationCodeScreenFactory>(
                    enterAuthenticationCodeScreenFactoryQualifier
                ),
                addProxyScreenFactory = get<AddProxyScreenFactory>(
                    addProxyScreenFactoryQualifier
                )
            )
        }

    }

}