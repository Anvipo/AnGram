package com.anvipo.angram.layers.application.coordinator.di

import com.anvipo.angram.layers.application.coordinator.ApplicationCoordinator
import com.anvipo.angram.layers.application.coordinator.ApplicationCoordinatorImp
import com.anvipo.angram.layers.application.coordinator.coordinatorsFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.layers.application.coordinator.coordinatorsFactory.ApplicationCoordinatorsFactoryImp
import com.anvipo.angram.layers.application.di.LaunchSystemModule.systemMessageSendChannelQualifier
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.routerQualifier
import com.anvipo.angram.layers.application.types.SystemMessageSendChannel
import com.anvipo.angram.dataLayer.di.GatewaysModule.applicationTDLibGatewayQualifier
import com.anvipo.angram.dataLayer.di.GatewaysModule.authorizationTDLibGatewayQualifier
import com.anvipo.angram.dataLayer.gateways.tdLib.application.ApplicationTDLibGateway
import com.anvipo.angram.dataLayer.gateways.tdLib.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.presentationLayer.flows.auth.coordinator.AuthorizationCoordinatorImp
import com.anvipo.angram.presentationLayer.flows.auth.coordinator.di.AuthorizationCoordinatorModule.authorizationScreensFactoryQualifier
import com.anvipo.angram.presentationLayer.flows.auth.coordinator.interfaces.AuthorizationCoordinator
import com.anvipo.angram.presentationLayer.flows.auth.coordinator.screensFactory.authorization.AuthorizationScreensFactory
import org.koin.android.ext.koin.androidApplication
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

        factory<ApplicationCoordinator>(applicationCoordinatorQualifier) {
            ApplicationCoordinatorImp(
                tdLibGateway = get<ApplicationTDLibGateway>(applicationTDLibGatewayQualifier),
                systemMessageSendChannel =
                get<SystemMessageSendChannel>(systemMessageSendChannelQualifier),
                coordinatorsFactory =
                get<ApplicationCoordinatorsFactory>(applicationCoordinatorsFactoryQualifier),
                context = androidApplication().applicationContext
            )
        }

        factory<AuthorizationCoordinator>(authorizationCoordinatorQualifier) {
            AuthorizationCoordinatorImp(
                router = get<Router>(routerQualifier),
                screensFactory = get<AuthorizationScreensFactory>(authorizationScreensFactoryQualifier),
                tdLibGateway = get<AuthorizationTDLibGateway>(authorizationTDLibGatewayQualifier),
                systemMessageSendChannel =
                get<SystemMessageSendChannel>(systemMessageSendChannelQualifier)
            )
        }

        factory<ApplicationCoordinatorsFactory>(applicationCoordinatorsFactoryQualifier) {
            ApplicationCoordinatorsFactoryImp(
                koinScope = this
            )
        }

    }

}