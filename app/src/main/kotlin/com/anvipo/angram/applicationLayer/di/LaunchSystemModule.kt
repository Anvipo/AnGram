package com.anvipo.angram.applicationLayer.di

import com.anvipo.angram.applicationLayer.coordinator.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationCoordinatorModule.applicationCoordinatorQualifier
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.applicationLayer.launchSystem.appActivity.presenter.AppPresenter
import com.anvipo.angram.applicationLayer.launchSystem.appActivity.presenter.AppPresenterImp
import com.anvipo.angram.applicationLayer.types.*
import com.anvipo.angram.businessLogicLayer.di.UseCasesModule.appUseCaseQualifier
import com.anvipo.angram.businessLogicLayer.useCases.app.AppUseCase
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.coreLayer.message.SystemMessage
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

        single<EnabledProxyIdSendChannel>(enabledProxyIdSendChannelQualifier) {
            get<EnabledProxyIdBroadcastChannel>(enabledProxyIdBroadcastChannelQualifier)
        }
        single<EnabledProxyIdReceiveChannel>(enabledProxyIdReceiveChannelQualifier) {
            get<EnabledProxyIdBroadcastChannel>(enabledProxyIdBroadcastChannelQualifier).openSubscription()
        }
        single<EnabledProxyIdBroadcastChannel>(enabledProxyIdBroadcastChannelQualifier) {
            BroadcastChannel<Int?>(Channel.CONFLATED)
        }

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
                useCase = get<AppUseCase>(appUseCaseQualifier),
                coordinator = get<ApplicationCoordinator>(
                    applicationCoordinatorQualifier
                ),
                enabledProxyIdReceiveChannel = get<EnabledProxyIdReceiveChannel>(
                    enabledProxyIdReceiveChannelQualifier
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
