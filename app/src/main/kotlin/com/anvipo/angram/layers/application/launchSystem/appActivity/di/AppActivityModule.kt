package com.anvipo.angram.layers.application.launchSystem.appActivity.di

import com.anvipo.angram.layers.application.coordinator.di.ApplicationCoordinatorModule.applicationCoordinatorQualifier
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.layers.application.launchSystem.appActivity.presenter.AppPresenter
import com.anvipo.angram.layers.application.launchSystem.appActivity.presenter.AppPresenterImp
import com.anvipo.angram.layers.businessLogic.di.UseCasesModule.appUseCaseQualifier
import com.anvipo.angram.layers.core.message.SystemMessage
import com.anvipo.angram.layers.data.di.GatewaysModule.tdLibClientHasBeenRecreatedReceiveChannelQualifier
import com.anvipo.angram.layers.global.types.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object AppActivityModule {

    val tdApiUpdateConnectionStateAppPresenterSendChannelQualifier: StringQualifier =
        named("connectionStateAppPresenterSendChannel")
    private val tdApiUpdateConnectionStateAppPresenterReceiveChannelQualifier =
        named("connectionStateAppPresenterReceiveChannel")
    private val tdApiUpdateConnectionStateAppPresenterBroadcastChannelQualifier =
        named("connectionStateAppPresenterBroadcastChannel")

    val enabledProxyIdSendChannelQualifier: StringQualifier = named("enabledProxyIdSendChannel")
    private val enabledProxyIdReceiveChannelQualifier = named("enabledProxyIdReceiveChannel")
    private val enabledProxyIdBroadcastChannelQualifier = named("enabledProxyIdBroadcastChannel")

    val systemMessageSendChannelQualifier: StringQualifier = named("systemMessageSendChannel")
    private val systemMessageReceiveChannelQualifier = named("systemMessageReceiveChannel")
    private val systemMessageBroadcastChannelQualifier = named("systemMessageBroadcastChannel")

    val appPresenterQualifier: StringQualifier = named("appPresenter")

    @ExperimentalCoroutinesApi
    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<TdApiUpdateConnectionStateSendChannel>(
            tdApiUpdateConnectionStateAppPresenterSendChannelQualifier
        ) {
            get(tdApiUpdateConnectionStateAppPresenterBroadcastChannelQualifier)
        }
        factory<TdApiUpdateConnectionStateReceiveChannel>(
            tdApiUpdateConnectionStateAppPresenterReceiveChannelQualifier
        ) {
            get<TdApiUpdateConnectionStateBroadcastChannel>(
                tdApiUpdateConnectionStateAppPresenterBroadcastChannelQualifier
            ).openSubscription()
        }
        single<TdApiUpdateConnectionStateBroadcastChannel>(
            tdApiUpdateConnectionStateAppPresenterBroadcastChannelQualifier
        ) {
            BroadcastChannel<TdApiUpdateConnectionState>(Channel.CONFLATED)
        }


        single<EnabledProxyIdSendChannel>(enabledProxyIdSendChannelQualifier) {
            get(enabledProxyIdBroadcastChannelQualifier)
        }
        factory<EnabledProxyIdReceiveChannel>(enabledProxyIdReceiveChannelQualifier) {
            get<EnabledProxyIdBroadcastChannel>(enabledProxyIdBroadcastChannelQualifier).openSubscription()
        }
        single<EnabledProxyIdBroadcastChannel>(enabledProxyIdBroadcastChannelQualifier) {
            BroadcastChannel<Int?>(Channel.CONFLATED)
        }


        single<SystemMessageSendChannel>(systemMessageSendChannelQualifier) {
            get(systemMessageBroadcastChannelQualifier)
        }
        factory<SystemMessageReceiveChannel>(systemMessageReceiveChannelQualifier) {
            get<SystemMessageBroadcastChannel>(systemMessageBroadcastChannelQualifier).openSubscription()
        }
        single<SystemMessageBroadcastChannel>(systemMessageBroadcastChannelQualifier) {
            BroadcastChannel<SystemMessage>(Channel.CONFLATED)
        }


        factory<AppPresenter>(appPresenterQualifier) {
            AppPresenterImp(
                useCase = get(appUseCaseQualifier),
                coordinatorFactoryMethod = { get(applicationCoordinatorQualifier) },
                enabledProxyIdReceiveChannel = get(enabledProxyIdReceiveChannelQualifier),
                systemMessageReceiveChannel = get(systemMessageReceiveChannelQualifier),
                tdApiUpdateConnectionStateReceiveChannel = get(
                    tdApiUpdateConnectionStateAppPresenterReceiveChannelQualifier
                ),
                tdLibClientHasBeenRecreatedReceiveChannel = get(
                    tdLibClientHasBeenRecreatedReceiveChannelQualifier
                ),
                resourceManager = get(resourceManagerQualifier)
            )
        }

    }

}