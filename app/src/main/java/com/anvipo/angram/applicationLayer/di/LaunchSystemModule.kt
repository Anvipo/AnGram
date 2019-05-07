package com.anvipo.angram.applicationLayer.di

import com.anvipo.angram.applicationLayer.types.*
import com.anvipo.angram.coreLayer.collections.MutableStack
import com.anvipo.angram.coreLayer.message.SystemMessage
import kotlinx.coroutines.channels.BroadcastChannel
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object LaunchSystemModule {

    internal val updateAuthorizationStateIMutableStack: StringQualifier = named("updateAuthorizationStateIMutableStack")
    internal val updateAuthorizationStateIReadOnlyStack: StringQualifier =
        named("updateAuthorizationStateIReadOnlyStack")

    internal val systemMessageReceiveChannel: StringQualifier = named("systemMessageReceiveChannel")
    internal val systemMessageSendChannel: StringQualifier = named("systemMessageSendChannel")
    private val systemMessageBroadcastChannel: StringQualifier = named("systemMessageBroadcastChannel")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

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
