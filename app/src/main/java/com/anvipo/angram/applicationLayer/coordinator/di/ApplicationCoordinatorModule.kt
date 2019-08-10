package com.anvipo.angram.applicationLayer.coordinator.di

import com.anvipo.angram.applicationLayer.coordinator.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.coordinator.ApplicationCoordinatorImp
import com.anvipo.angram.applicationLayer.coordinator.coordinatorsFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.applicationLayer.coordinator.coordinatorsFactory.ApplicationCoordinatorsFactoryImp
import com.anvipo.angram.applicationLayer.di.LaunchSystemModule.systemMessageSendChannelQualifier
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.routerQualifier
import com.anvipo.angram.applicationLayer.types.SystemMessageSendChannel
import com.anvipo.angram.dataLayer.di.GatewaysModule.applicationTDLibGatewayQualifier
import com.anvipo.angram.dataLayer.di.GatewaysModule.authorizationTDLibGatewayQualifier
import com.anvipo.angram.dataLayer.gateways.tdLib.application.ApplicationTDLibGateway
import com.anvipo.angram.dataLayer.gateways.tdLib.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.AuthorizationCoordinatorImp
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.di.AuthorizationCoordinatorModule.authorizationScreensFactoryQualifier
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.interfaces.AuthorizationCoordinator
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactory
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.Router

object ApplicationCoordinatorModule {

    val applicationCoordinatorQualifier: StringQualifier = named("applicationCoordinator")
    val authorizationCoordinatorQualifier: StringQualifier = named("authorizationCoordinator")

    private val applicationCoordinatorsFactoryQualifier = named("applicationCoordinatorsFactory")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<ApplicationCoordinator>(applicationCoordinatorQualifier) {
            ApplicationCoordinatorImp(
                tdLibGateway = get<ApplicationTDLibGateway>(applicationTDLibGatewayQualifier),
                systemMessageSendChannel =
                get<SystemMessageSendChannel>(systemMessageSendChannelQualifier),
                coordinatorsFactory =
                get<ApplicationCoordinatorsFactory>(applicationCoordinatorsFactoryQualifier),
                context = get()
            )
        }

        single<AuthorizationCoordinator>(authorizationCoordinatorQualifier) {
            AuthorizationCoordinatorImp(
                router = get<Router>(routerQualifier),
                screensFactory = get<AuthorizationScreensFactory>(authorizationScreensFactoryQualifier),
                tdLibGateway = get<AuthorizationTDLibGateway>(authorizationTDLibGatewayQualifier),
                systemMessageSendChannel =
                get<SystemMessageSendChannel>(systemMessageSendChannelQualifier)
            )
        }

        single<ApplicationCoordinatorsFactory>(applicationCoordinatorsFactoryQualifier) {
            ApplicationCoordinatorsFactoryImp(
                koinScope = this
            )
        }

    }

}