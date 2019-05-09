package com.anvipo.angram.applicationLayer.di

import com.anvipo.angram.applicationLayer.coordinator.ApplicationCoordinatorOutput
import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationRootCoordinatorModule
import com.anvipo.angram.applicationLayer.launchSystem.appActivity.presenter.AppPresenterImp
import com.anvipo.angram.applicationLayer.types.*
import com.anvipo.angram.coreLayer.message.SystemMessage
import kotlinx.coroutines.channels.BroadcastChannel
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object LaunchSystemModule {

    internal val updateAuthorizationStateMutableList: StringQualifier =
        named("updateAuthorizationStateMutableList")
    internal val updateAuthorizationStateList: StringQualifier = named("updateAuthorizationStateList")

    private val systemMessageReceiveChannel: StringQualifier = named("systemMessageReceiveChannel")
    internal val systemMessageSendChannel: StringQualifier = named("systemMessageSendChannel")
    private val systemMessageBroadcastChannel: StringQualifier = named("systemMessageBroadcastChannel")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<UpdateAuthorizationStateList>(updateAuthorizationStateList) {
            get<UpdateAuthorizationStateList>(updateAuthorizationStateMutableList)
        }

        single<UpdateAuthorizationStateMutableList>(updateAuthorizationStateMutableList) {
            mutableListOf<TdApi.UpdateAuthorizationState>()
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
