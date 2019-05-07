package com.anvipo.angram.applicationLayer.di

import com.anvipo.angram.applicationLayer.navigation.coordinator.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.navigation.coordinator.ApplicationCoordinatorImp
import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.applicationLayer.navigation.coordinator.di.ApplicationRootCoordinatorModule.applicationCoordinatorsFactory
import com.anvipo.angram.applicationLayer.types.*
import com.anvipo.angram.businessLogicLayer.di.GatewaysModule.tdLibGateway
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.coreLayer.collections.MutableStack
import com.anvipo.angram.coreLayer.message.SystemMessage
import kotlinx.coroutines.channels.BroadcastChannel
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.Router

object LaunchSystemModule {

    internal val updateAuthorizationStateIMutableStack: StringQualifier = named("updateAuthorizationStateIMutableStack")
    internal val updateAuthorizationStateIReadOnlyStack: StringQualifier =
        named("updateAuthorizationStateIReadOnlyStack")

    internal val applicationCoordinator: StringQualifier = named("applicationCoordinator")

    internal val systemMessageReceiveChannel: StringQualifier = named("systemMessageReceiveChannel")
    internal val systemMessageSendChannel: StringQualifier = named("systemMessageSendChannel")
    private val systemMessageBroadcastChannel: StringQualifier = named("systemMessageBroadcastChannel")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<ApplicationCoordinator>(applicationCoordinator) {
            ApplicationCoordinatorImp(
                coordinatorsFactory = get<ApplicationCoordinatorsFactory>(applicationCoordinatorsFactory),
                router = get<Router>(SystemInfrastructureModule.router),
                tdLibGateway = get<TDLibGateway>(tdLibGateway),
                systemMessageSendChannel = get<SystemMessageSendChannel>(systemMessageSendChannel)
            )
        }

        single<UpdateAuthorizationStateIReadOnlyStack>(updateAuthorizationStateIReadOnlyStack) {
            get<UpdateAuthorizationStateIMutableStack>(updateAuthorizationStateIMutableStack)
        }

        single<UpdateAuthorizationStateIMutableStack>(updateAuthorizationStateIMutableStack) {
            MutableStack<TdApi.UpdateAuthorizationState>()
        }

        single<SystemMessageSendChannel>(systemMessageSendChannel) {
            get<SystemMessageBroadcastChannel>(systemMessageBroadcastChannel)
        }
        single<SystemMessageReceiveChannel>(systemMessageReceiveChannel) {
            get<SystemMessageBroadcastChannel>(systemMessageBroadcastChannel).openSubscription()
        }
        single<SystemMessageBroadcastChannel>(systemMessageBroadcastChannel) {
            BroadcastChannel<SystemMessage>(1)
        }

    }

}
