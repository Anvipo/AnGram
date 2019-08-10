package com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.di

import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.addProxy.AddProxyScreenFactory
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.authorization.AuthorizationScreensFactory
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.authorization.AuthorizationScreensFactoryImp
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.enterAuthenticationCode.EnterAuthenticationCodeScreenFactory
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.enterAuthenticationPassword.EnterAuthenticationPasswordScreenFactory
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.enterPhoneNumber.EnterPhoneNumberScreenFactory
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.di.AddProxyModule.addProxyScreenFactoryQualifier
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule.enterAuthenticationCodeScreenFactoryQualifier
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationPassword.di.EnterAuthenticationPasswordModule.enterAuthenticationPasswordScreenFactoryQualifier
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterPhoneNumber.di.EnterPhoneNumberModule.enterPhoneNumberScreenFactoryQualifier
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