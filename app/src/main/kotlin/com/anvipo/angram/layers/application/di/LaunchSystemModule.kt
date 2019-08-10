package com.anvipo.angram.layers.application.di

import com.anvipo.angram.layers.application.coordinator.di.ApplicationCoordinatorModule.applicationCoordinatorQualifier
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.layers.application.launchSystem.appActivity.presenter.AppPresenter
import com.anvipo.angram.layers.application.launchSystem.appActivity.presenter.AppPresenterImp
import com.anvipo.angram.layers.application.types.*
import com.anvipo.angram.layers.businessLogic.di.UseCasesModule.appUseCaseQualifier
import com.anvipo.angram.layers.core.message.SystemMessage
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

@Suppress("EXPERIMENTAL_API_USAGE")
object LaunchSystemModule {

    private val enabledProxyIdReceiveChannelQualifier = named("enabledProxyIdReceiveChannel")
    val enabledProxyIdSendChannelQualifier: StringQualifier = named("enabledProxyIdSendChannel")
    private val enabledProxyIdBroadcastChannelQualifier = named("enabledProxyIdBroadcastChannel")


    private val systemMessageReceiveChannelQualifier = named("systemMessageReceiveChannel")
    val systemMessageSendChannelQualifier: StringQualifier = named("systemMessageSendChannel")
    private val systemMessageBroadcastChannelQualifier = named("systemMessageBroadcastChannel")

    private val connectionStateAppReceiveChannelQualifier = named("connectionStateAppReceiveChannel")
    val connectionStateAppSendChannelQualifier: StringQualifier = named("connectionStateAppSendChannel")
    private val connectionStateAppBroadcastChannelQualifier = named("connectionStateAppBroadcastChannel")


    val appPresenterQualifier: StringQualifier = named("appPresenter")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        factory<EnabledProxyIdSendChannel>(enabledProxyIdSendChannelQualifier) {
            get(enabledProxyIdBroadcastChannelQualifier)
        }
        factory<EnabledProxyIdReceiveChannel>(enabledProxyIdReceiveChannelQualifier) {
            get<EnabledProxyIdBroadcastChannel>(enabledProxyIdBroadcastChannelQualifier).openSubscription()
        }
        single<EnabledProxyIdBroadcastChannel>(enabledProxyIdBroadcastChannelQualifier) {
            BroadcastChannel<Int?>(Channel.CONFLATED)
        }

        factory<SystemMessageSendChannel>(systemMessageSendChannelQualifier) {
            get(systemMessageBroadcastChannelQualifier)
        }
        factory<SystemMessageReceiveChannel>(systemMessageReceiveChannelQualifier) {
            get<SystemMessageBroadcastChannel>(systemMessageBroadcastChannelQualifier).openSubscription()
        }
        single<SystemMessageBroadcastChannel>(systemMessageBroadcastChannelQualifier) {
            BroadcastChannel<SystemMessage>(Channel.CONFLATED)
        }

        factory<ConnectionStateSendChannel>(connectionStateAppSendChannelQualifier) {
            get(connectionStateAppBroadcastChannelQualifier)
        }
        factory<ConnectionStateReceiveChannel>(connectionStateAppReceiveChannelQualifier) {
            get<ConnectionStateBroadcastChannel>(connectionStateAppBroadcastChannelQualifier).openSubscription()
        }
        single<ConnectionStateBroadcastChannel>(connectionStateAppBroadcastChannelQualifier) {
            BroadcastChannel<TdApi.ConnectionState>(Channel.CONFLATED)
        }

        factory<AppPresenter>(appPresenterQualifier) {
            AppPresenterImp(
                useCase = get(appUseCaseQualifier),
                coordinator = get(applicationCoordinatorQualifier),
                enabledProxyIdReceiveChannel = get(enabledProxyIdReceiveChannelQualifier),
                systemMessageReceiveChannel = get(systemMessageReceiveChannelQualifier),
                connectionStateReceiveChannel = get(connectionStateAppReceiveChannelQualifier),
                resourceManager = get(resourceManagerQualifier)
            )
        }

    }

}
