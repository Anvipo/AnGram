package com.anvipo.angram.applicationLayer.coordinator.di

import com.anvipo.angram.applicationLayer.coordinator.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.coordinator.ApplicationCoordinatorImp
import com.anvipo.angram.applicationLayer.di.LaunchSystemModule.systemMessageSendChannelQualifier
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.routerQualifier
import com.anvipo.angram.applicationLayer.types.SystemMessageSendChannel
import com.anvipo.angram.dataLayer.di.GatewaysModule.applicationTDLibGatewayQualifier
import com.anvipo.angram.dataLayer.di.GatewaysModule.authorizationTDLibGatewayQualifier
import com.anvipo.angram.dataLayer.gateways.tdLib.application.ApplicationTDLibGateway
import com.anvipo.angram.dataLayer.gateways.tdLib.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.AuthorizationCoordinatorImp
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.interfaces.AuthorizationCoordinator
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactoryImp
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.Router

object ApplicationCoordinatorModule {

    internal val applicationCoordinatorQualifier = named("applicationCoordinator")
    internal val authorizationCoordinatorQualifier = named("authorizationCoordinator")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<ApplicationCoordinator>(applicationCoordinatorQualifier) {
            ApplicationCoordinatorImp(
                tdLibGateway = get<ApplicationTDLibGateway>(applicationTDLibGatewayQualifier),
                systemMessageSendChannel =
                get<SystemMessageSendChannel>(systemMessageSendChannelQualifier),
                authorizationCoordinator =
                get<AuthorizationCoordinator>(authorizationCoordinatorQualifier),
                context = get()
            )
        }

        single<AuthorizationCoordinator>(authorizationCoordinatorQualifier) {
            AuthorizationCoordinatorImp(
                router = get<Router>(routerQualifier),
                screensFactory = AuthorizationScreensFactoryImp,
                authorizationTDLibGateway = get<AuthorizationTDLibGateway>(authorizationTDLibGatewayQualifier),
                systemMessageSendChannel =
                get<SystemMessageSendChannel>(systemMessageSendChannelQualifier)
            )
        }

    }

}