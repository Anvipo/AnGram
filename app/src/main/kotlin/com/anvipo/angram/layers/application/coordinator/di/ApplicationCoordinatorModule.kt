package com.anvipo.angram.layers.application.coordinator.di

import com.anvipo.angram.layers.application.coordinator.ApplicationCoordinator
import com.anvipo.angram.layers.application.coordinator.ApplicationCoordinatorImpl
import com.anvipo.angram.layers.application.coordinator.coordinatorsFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.layers.application.coordinator.coordinatorsFactory.ApplicationCoordinatorsFactoryImpl
import com.anvipo.angram.layers.application.tdApiHelper.TdApiHelper.tdClientScope
import com.anvipo.angram.layers.application.tdApiHelper.di.TdApiHelperModule.systemMessageSendChannelQualifier
import com.anvipo.angram.layers.data.di.GatewaysModule.tdClientScopeQualifier
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateBroadcastChannel
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


    @ExperimentalCoroutinesApi
    val module: Module = module {

        single<ApplicationCoordinatorsFactory> {
            ApplicationCoordinatorsFactoryImpl
        }

        scope(tdClientScopeQualifier) {

            scoped<ApplicationCoordinator> {
                ApplicationCoordinatorImpl(
                    koinScope = this,
                    coordinatorsFactory = get(),
                    tdLibGateway = tdClientScope.get(),
                    tdApiUpdateAuthorizationStateReceiveChannel =
                    get(tdApiUpdateAuthorizationStateApplicationCoordinatorReceiveChannelQualifier),
                    systemMessageSendChannel = get(systemMessageSendChannelQualifier)
                )
            }

        }


        single<TdApiUpdateAuthorizationStateSendChannel>(
            tdApiUpdateAuthorizationStateApplicationCoordinatorSendChannelQualifier
        ) {
            get(tdApiUpdateAuthorizationStateApplicationCoordinatorBroadcastChannelQualifier)
        }
        factory(
            tdApiUpdateAuthorizationStateApplicationCoordinatorReceiveChannelQualifier
        ) {
            get<TdApiUpdateAuthorizationStateBroadcastChannel>(
                tdApiUpdateAuthorizationStateApplicationCoordinatorBroadcastChannelQualifier
            ).openSubscription()
        }
        single<TdApiUpdateAuthorizationStateBroadcastChannel>(
            tdApiUpdateAuthorizationStateApplicationCoordinatorBroadcastChannelQualifier
        ) {
            BroadcastChannel(Channel.CONFLATED)
        }

    }

}