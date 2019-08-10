package com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.di

import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.addProxyScreenFactory.AddProxyScreenFactory
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactory
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactoryImp
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.enterAuthCodeScreenFactory.EnterAuthenticationCodeScreenFactory
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.enterPasswordScreenFactory.EnterAuthenticationPasswordScreenFactory
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.enterPhoneNumberScreenFactory.EnterPhoneNumberScreenFactory
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

        single<AuthorizationScreensFactory>(authorizationScreensFactoryQualifier) {
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