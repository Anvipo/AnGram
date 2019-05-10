package com.anvipo.angram.applicationLayer.di

import com.anvipo.angram.applicationLayer.coordinator.ApplicationCoordinatorOutput
import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationRootCoordinatorModule
import com.anvipo.angram.applicationLayer.launchSystem.appActivity.presenter.AppPresenterImp
import com.anvipo.angram.applicationLayer.types.SystemMessageBroadcastChannel
import com.anvipo.angram.applicationLayer.types.SystemMessageReceiveChannel
import com.anvipo.angram.applicationLayer.types.SystemMessageSendChannel
import com.anvipo.angram.coreLayer.message.SystemMessage
import kotlinx.coroutines.channels.BroadcastChannel
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object LaunchSystemModule {

    private val systemMessageReceiveChannel: StringQualifier = named("systemMessageReceiveChannel")
    internal val systemMessageSendChannel: StringQualifier = named("systemMessageSendChannel")
    private val systemMessageBroadcastChannel: StringQualifier = named("systemMessageBroadcastChannel")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<SystemMessageSendChannel>(systemMessageSendChannel) {
            get<SystemMessageBroadcastChannel>(systemMessageBroadcastChannel)
        }
        single<SystemMessageReceiveChannel>(systemMessageReceiveChannel) {
            get<SystemMessageBroadcastChannel>(systemMessageBroadcastChannel).openSubscription()
        }
        single<SystemMessageBroadcastChannel>(systemMessageBroadcastChannel) {
            BroadcastChannel<SystemMessage>(1)
        }

        single<AppPresenterImp>(appPresenter) {
            AppPresenterImp(
                coordinator = get<ApplicationCoordinatorOutput>(
                    ApplicationRootCoordinatorModule.applicationCoordinator
                ),
                systemMessageReceiveChannel = get<SystemMessageReceiveChannel>(
                    systemMessageReceiveChannel
                )
            )
        }

    }

    internal val appPresenter: StringQualifier = named("appPresenter")

}
