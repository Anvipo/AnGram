package com.anvipo.angram.applicationLayer.di

import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationRootCoordinatorModule.applicationCoordinatorQualifier
import com.anvipo.angram.applicationLayer.coordinator.interfaces.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.applicationLayer.launchSystem.appActivity.presenter.AppPresenter
import com.anvipo.angram.applicationLayer.launchSystem.appActivity.presenter.AppPresenterImp
import com.anvipo.angram.applicationLayer.types.*
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.coreLayer.message.SystemMessage
import kotlinx.coroutines.channels.BroadcastChannel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

object LaunchSystemModule {

    private val systemMessageReceiveChannelQualifier = named("systemMessageReceiveChannel")
    internal val systemMessageSendChannelQualifier = named("systemMessageSendChannel")
    private val systemMessageBroadcastChannelQualifier = named("systemMessageBroadcastChannel")

    private val connectionStateReceiveChannelQualifier = named("connectionStateReceiveChannel")
    internal val connectionStateSendChannelQualifier = named("connectionStateSendChannel")
    private val connectionStateBroadcastChannelQualifier = named("connectionStateBroadcastChannel")


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
            BroadcastChannel<SystemMessage>(1)
        }

        single<ConnectionStateSendChannel>(connectionStateSendChannelQualifier) {
            get<ConnectionStateBroadcastChannel>(connectionStateBroadcastChannelQualifier)
        }
        single<ConnectionStateReceiveChannel>(connectionStateReceiveChannelQualifier) {
            get<ConnectionStateBroadcastChannel>(connectionStateBroadcastChannelQualifier).openSubscription()
        }
        single<ConnectionStateBroadcastChannel>(connectionStateBroadcastChannelQualifier) {
            BroadcastChannel<ConnectionState>(1)
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
                    connectionStateReceiveChannelQualifier
                ),
                resourceManager = get<ResourceManager>(resourceManagerQualifier)
            )
        }

    }

}
