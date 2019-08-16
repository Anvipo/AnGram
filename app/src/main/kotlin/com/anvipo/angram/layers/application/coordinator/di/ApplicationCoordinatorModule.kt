package com.anvipo.angram.layers.application.coordinator.di

import com.anvipo.angram.layers.application.coordinator.ApplicationCoordinator
import com.anvipo.angram.layers.application.coordinator.ApplicationCoordinatorImpl
import com.anvipo.angram.layers.application.coordinator.coordinatorsFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.layers.application.coordinator.coordinatorsFactory.ApplicationCoordinatorsFactoryImpl
import com.anvipo.angram.layers.application.launchSystem.appActivity.di.AppActivityModule.systemMessageSendChannelQualifier
import com.anvipo.angram.layers.data.di.GatewaysModule.applicationTDLibGatewayQualifier
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationState
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateBroadcastChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateReceiveChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateSendChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ApplicationCoordinatorModule {

    val tdApiUpdateAuthorizationStateApplicationCoordinatorSendChannelQualifier: StringQualifier =
        named("tdApiUpdateAuthorizationStateApplicationCoordinatorSendChannel")
    private val tdApiUpdateAuthorizationStateApplicationCoordinatorReceiveChannelQualifier =
        named("tdApiUpdateAuthorizationStateApplicationCoordinatorReceiveChannel")
    private val tdApiUpdateAuthorizationStateApplicationCoordinatorBroadcastChannelQualifier =
        named("tdApiUpdateAuthorizationStateApplicationCoordinatorBroadcastChannel")

    val applicationCoordinatorQualifier: StringQualifier = named("applicationCoordinator")

    private val applicationCoordinatorsFactoryQualifier = named("applicationCoordinatorsFactory")

    @ExperimentalCoroutinesApi
    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<TdApiUpdateAuthorizationStateSendChannel>(
            tdApiUpdateAuthorizationStateApplicationCoordinatorSendChannelQualifier
        ) {
            get(tdApiUpdateAuthorizationStateApplicationCoordinatorBroadcastChannelQualifier)
        }
        factory<TdApiUpdateAuthorizationStateReceiveChannel>(
            tdApiUpdateAuthorizationStateApplicationCoordinatorReceiveChannelQualifier
        ) {
            get<TdApiUpdateAuthorizationStateBroadcastChannel>(
                tdApiUpdateAuthorizationStateApplicationCoordinatorBroadcastChannelQualifier
            ).openSubscription()
        }
        single<TdApiUpdateAuthorizationStateBroadcastChannel>(
            tdApiUpdateAuthorizationStateApplicationCoordinatorBroadcastChannelQualifier
        ) {
            BroadcastChannel<TdApiUpdateAuthorizationState>(Channel.CONFLATED)
        }

        factory<ApplicationCoordinator>(applicationCoordinatorQualifier) {
            ApplicationCoordinatorImpl(
                coordinatorsFactory = get(applicationCoordinatorsFactoryQualifier),
                tdLibGateway = get(applicationTDLibGatewayQualifier),
                tdApiUpdateAuthorizationStateReceiveChannel =
                get(tdApiUpdateAuthorizationStateApplicationCoordinatorReceiveChannelQualifier),
                systemMessageSendChannel = get(systemMessageSendChannelQualifier)
            )
        }

        factory<ApplicationCoordinatorsFactory>(applicationCoordinatorsFactoryQualifier) {
            ApplicationCoordinatorsFactoryImpl(
                koinScope = this
            )
        }

    }

}