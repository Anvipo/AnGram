package com.anvipo.angram.applicationLayer.di

import com.anvipo.angram.applicationLayer.coordinator.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationCoordinatorModule.applicationCoordinatorQualifier
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.applicationLayer.launchSystem.appActivity.presenter.AppPresenter
import com.anvipo.angram.applicationLayer.launchSystem.appActivity.presenter.AppPresenterImp
import com.anvipo.angram.applicationLayer.types.*
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.coreLayer.message.SystemMessage
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

@Suppress("EXPERIMENTAL_API_USAGE")
object LaunchSystemModule {

    private val systemMessageReceiveChannelQualifier = named("systemMessageReceiveChannel")
    internal val systemMessageSendChannelQualifier = named("systemMessageSendChannel")
    private val systemMessageBroadcastChannelQualifier = named("systemMessageBroadcastChannel")

    private val connectionStateAppReceiveChannelQualifier = named("connectionStateAppReceiveChannel")
    internal val connectionStateAppSendChannelQualifier = named("connectionStateAppSendChannel")
    private val connectionStateAppBroadcastChannelQualifier = named("connectionStateAppBroadcastChannel")


    internal val appPresenterQualifier = named("appPresenter")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<SystemMessageSendChannel>(systemMessageSendChannelQualifier) {
            get<SystemMessageBroadcastChannel>(systemMessageBroadcastChannelQualifier)
        }
        single<SystemMessageReceiveChannel>(systemMessageReceiveChannelQualifier) {
            get<SystemMessageBroadcastChannel>(systemMessageBroadcastChannelQualifier).openSubscription()
        }
        single<SystemMessageBroadcastChannel>(systemMessageBroadcastChannelQualifier) {
            BroadcastChannel<SystemMessage>(Channel.CONFLATED)
        }

        single<ConnectionStateSendChannel>(connectionStateAppSendChannelQualifier) {
            get<ConnectionStateBroadcastChannel>(connectionStateAppBroadcastChannelQualifier)
        }
        single<ConnectionStateReceiveChannel>(connectionStateAppReceiveChannelQualifier) {
            get<ConnectionStateBroadcastChannel>(connectionStateAppBroadcastChannelQualifier).openSubscription()
        }
        single<ConnectionStateBroadcastChannel>(connectionStateAppBroadcastChannelQualifier) {
            BroadcastChannel<TdApi.ConnectionState>(Channel.CONFLATED)
        }

        single<AppPresenter>(appPresenterQualifier) {
            AppPresenterImp(
                coordinator = get<ApplicationCoordinator>(
                    applicationCoordinatorQualifier
                ),
                systemMessageReceiveChannel = get<SystemMessageReceiveChannel>(
                    systemMessageReceiveChannelQualifier
                ),
                connectionStateReceiveChannel = get<ConnectionStateReceiveChannel>(
                    connectionStateAppReceiveChannelQualifier
                ),
                resourceManager = get<ResourceManager>(resourceManagerQualifier)
            )
        }

    }

}
