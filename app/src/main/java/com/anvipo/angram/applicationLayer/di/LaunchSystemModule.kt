package com.anvipo.angram.applicationLayer.di

import com.anvipo.angram.applicationLayer.coordinator.ApplicationCoordinatorOutput
import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationRootCoordinatorModule.applicationCoordinatorQualifier
import com.anvipo.angram.applicationLayer.launchSystem.appActivity.presenter.AppPresenter
import com.anvipo.angram.applicationLayer.launchSystem.appActivity.presenter.AppPresenterImp
import com.anvipo.angram.applicationLayer.types.SystemMessageBroadcastChannel
import com.anvipo.angram.applicationLayer.types.SystemMessageReceiveChannel
import com.anvipo.angram.applicationLayer.types.SystemMessageSendChannel
import com.anvipo.angram.coreLayer.message.SystemMessage
import kotlinx.coroutines.channels.BroadcastChannel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

object LaunchSystemModule {

    private val systemMessageReceiveChannelQualifier = named("systemMessageReceiveChannel")
    internal val systemMessageSendChannelQualifier = named("systemMessageSendChannel")
    private val systemMessageBroadcastChannelQualifier = named("systemMessageBroadcastChannel")

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

        single<AppPresenter>(appPresenterQualifier) {
            AppPresenterImp(
                coordinator = get<ApplicationCoordinatorOutput>(
                    applicationCoordinatorQualifier
                ),
                systemMessageReceiveChannel = get<SystemMessageReceiveChannel>(
                    systemMessageReceiveChannelQualifier
                )
            )
        }

    }

}
