package com.anvipo.angram.layers.application.coordinator.di

import com.anvipo.angram.layers.application.coordinator.ApplicationCoordinator
import com.anvipo.angram.layers.application.coordinator.ApplicationCoordinatorImp
import com.anvipo.angram.layers.application.coordinator.coordinatorsFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.layers.application.coordinator.coordinatorsFactory.ApplicationCoordinatorsFactoryImp
import com.anvipo.angram.layers.application.di.LaunchSystemModule.systemMessageSendChannelQualifier
import com.anvipo.angram.layers.data.di.GatewaysModule.applicationTDLibGatewayQualifier
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ApplicationCoordinatorModule {

    val applicationCoordinatorQualifier: StringQualifier = named("applicationCoordinator")

    private val applicationCoordinatorsFactoryQualifier = named("applicationCoordinatorsFactory")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<ApplicationCoordinator>(applicationCoordinatorQualifier) {
            ApplicationCoordinatorImp(
                coordinatorsFactory = get(applicationCoordinatorsFactoryQualifier),
                tdLibGateway = get(applicationTDLibGatewayQualifier),
                systemMessageSendChannel = get(systemMessageSendChannelQualifier)
            )
        }

        single<ApplicationCoordinatorsFactory>(applicationCoordinatorsFactoryQualifier) {
            ApplicationCoordinatorsFactoryImp(
                koinScope = this
            )
        }

    }

}