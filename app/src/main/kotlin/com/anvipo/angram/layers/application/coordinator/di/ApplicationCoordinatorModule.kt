package com.anvipo.angram.layers.application.coordinator.di

import com.anvipo.angram.layers.application.coordinator.ApplicationCoordinator
import com.anvipo.angram.layers.application.coordinator.ApplicationCoordinatorImp
import com.anvipo.angram.layers.application.coordinator.coordinatorsFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.layers.application.coordinator.coordinatorsFactory.ApplicationCoordinatorsFactoryImp
import com.anvipo.angram.layers.application.di.LaunchSystemModule.systemMessageSendChannelQualifier
import com.anvipo.angram.layers.application.types.SystemMessageSendChannel
import com.anvipo.angram.layers.data.di.GatewaysModule.applicationTDLibGatewayQualifier
import com.anvipo.angram.layers.data.gateways.tdLib.application.ApplicationTDLibGateway
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ApplicationCoordinatorModule {

    val applicationCoordinatorQualifier: StringQualifier = named("applicationCoordinator")

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

        factory<ApplicationCoordinatorsFactory>(applicationCoordinatorsFactoryQualifier) {
            ApplicationCoordinatorsFactoryImp(
                koinScope = this
            )
        }

    }

}