package com.anvipo.angram.layers.application.launchSystem.appActivity.di

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anvipo.angram.layers.application.coordinator.di.ApplicationCoordinatorModule.applicationCoordinatorQualifier
import com.anvipo.angram.layers.application.di.LaunchSystemModule.tdLibClientHasBeenRecreatedReceiveChannelQualifier
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.layers.application.launchSystem.App
import com.anvipo.angram.layers.application.launchSystem.appActivity.viewModel.AppViewModel
import com.anvipo.angram.layers.application.launchSystem.appActivity.viewModel.AppViewModelImpl
import com.anvipo.angram.layers.businessLogic.di.UseCasesModule.appUseCaseQualifier
import com.anvipo.angram.layers.core.message.SystemMessage
import com.anvipo.angram.layers.global.types.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import org.koin.core.KoinComponent
import org.koin.core.get
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

    private val appViewModelQualifier: StringQualifier = named("appViewModel")
    val appViewModelFactoryQualifier: StringQualifier = named("appViewModelFactory")

    private object AppViewModelFactory : ViewModelProvider.NewInstanceFactory(), KoinComponent {
        @SuppressLint("SyntheticAccessor")
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return get<AppViewModel>(appViewModelQualifier) as T
        }
    }

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

        single<AppViewModelFactory>(appViewModelFactoryQualifier) {
            AppViewModelFactory
        }

        factory<AppViewModel>(appViewModelQualifier) {
            AppViewModelImpl(
                useCase = get(appUseCaseQualifier),
                coordinatorFactoryMethod = { App.tdClientScope.get(applicationCoordinatorQualifier) },
                enabledProxyIdReceiveChannel = get(enabledProxyIdReceiveChannelQualifier),
                systemMessageReceiveChannel = get(systemMessageReceiveChannelQualifier),
                tdApiUpdateConnectionStateReceiveChannel = get(
                    tdApiUpdateConnectionStateAppPresenterReceiveChannelQualifier
                ),
                resourceManager = get(resourceManagerQualifier),
                tdLibClientHasBeenRecreatedReceiveChannel =
                get(tdLibClientHasBeenRecreatedReceiveChannelQualifier)
            )
        }

    }

}