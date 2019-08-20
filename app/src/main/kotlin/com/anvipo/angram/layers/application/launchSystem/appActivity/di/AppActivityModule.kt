package com.anvipo.angram.layers.application.launchSystem.appActivity.di

import com.anvipo.angram.layers.application.di.LaunchSystemModule.enabledProxyIdReceiveChannelQualifier
import com.anvipo.angram.layers.application.di.LaunchSystemModule.systemMessageReceiveChannelQualifier
import com.anvipo.angram.layers.application.di.LaunchSystemModule.tdLibClientHasBeenRecreatedReceiveChannelQualifier
import com.anvipo.angram.layers.application.launchSystem.App
import com.anvipo.angram.layers.application.launchSystem.appActivity.viewModel.AppViewModel
import com.anvipo.angram.layers.application.launchSystem.appActivity.viewModel.AppViewModelFactory
import com.anvipo.angram.layers.application.launchSystem.appActivity.viewModel.AppViewModelImpl
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateBroadcastChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateConnectionStateSendChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object AppActivityModule {

    val tdApiUpdateConnectionStateAppViewModelSendChannelQualifier: StringQualifier =
        named("connectionStateAppViewModelSendChannel")
    private val tdApiUpdateConnectionStateAppViewModelReceiveChannelQualifier =
        named("connectionStateAppViewModelReceiveChannel")
    private val tdApiUpdateConnectionStateAppViewModelBroadcastChannelQualifier =
        named("connectionStateAppViewModelBroadcastChannel")


    @ExperimentalCoroutinesApi
    val module: Module = module {

        single<TdApiUpdateConnectionStateSendChannel>(
            tdApiUpdateConnectionStateAppViewModelSendChannelQualifier
        ) {
            get(tdApiUpdateConnectionStateAppViewModelBroadcastChannelQualifier)
        }
        factory(
            tdApiUpdateConnectionStateAppViewModelReceiveChannelQualifier
        ) {
            get<TdApiUpdateConnectionStateBroadcastChannel>(
                tdApiUpdateConnectionStateAppViewModelBroadcastChannelQualifier
            ).openSubscription()
        }
        single<TdApiUpdateConnectionStateBroadcastChannel>(
            tdApiUpdateConnectionStateAppViewModelBroadcastChannelQualifier
        ) {
            BroadcastChannel(Channel.CONFLATED)
        }


        single {
            AppViewModelFactory
        }

        factory<AppViewModel> {
            AppViewModelImpl(
                useCaseFactoryMethod = { App.tdClientScope.get() },
                coordinatorFactoryMethod = { App.tdClientScope.get() },
                enabledProxyIdReceiveChannel = get(enabledProxyIdReceiveChannelQualifier),
                systemMessageReceiveChannel = get(systemMessageReceiveChannelQualifier),
                tdApiUpdateConnectionStateReceiveChannel =
                get(tdApiUpdateConnectionStateAppViewModelReceiveChannelQualifier),
                resourceManager = get(),
                tdLibClientHasBeenRecreatedReceiveChannel =
                get(tdLibClientHasBeenRecreatedReceiveChannelQualifier)
            )
        }

    }

}