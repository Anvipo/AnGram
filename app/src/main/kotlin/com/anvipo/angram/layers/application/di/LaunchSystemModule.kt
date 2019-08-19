package com.anvipo.angram.layers.application.di

import com.anvipo.angram.layers.application.launchSystem.appActivity.types.TDLibClientHasBeenRecreatedBroadcastChannel
import com.anvipo.angram.layers.application.launchSystem.appActivity.types.TDLibClientHasBeenRecreatedReceiveChannel
import com.anvipo.angram.layers.application.launchSystem.appActivity.types.TDLibClientHasBeenRecreatedSendChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object LaunchSystemModule {

    val tdLibClientHasBeenRecreatedReceiveChannelQualifier: StringQualifier =
        named("tdLibClientHasBeenRecreatedReceiveChannel")
    val tdLibClientHasBeenRecreatedSendChannelQualifier: StringQualifier =
        named("tdLibClientHasBeenRecreatedSendChannel")
    private val tdLibClientHasBeenRecreatedBroadcastChannelQualifier =
        named("tdLibClientHasBeenRecreatedBroadcastChannel")


    @ExperimentalCoroutinesApi
    val module: Module = module {

        single<TDLibClientHasBeenRecreatedSendChannel>(
            tdLibClientHasBeenRecreatedSendChannelQualifier
        ) {
            get(tdLibClientHasBeenRecreatedBroadcastChannelQualifier)
        }
        factory(
            tdLibClientHasBeenRecreatedReceiveChannelQualifier
        ) {
            get<TDLibClientHasBeenRecreatedBroadcastChannel>(
                tdLibClientHasBeenRecreatedBroadcastChannelQualifier
            ).openSubscription()
        }
        single<TDLibClientHasBeenRecreatedBroadcastChannel>(
            tdLibClientHasBeenRecreatedBroadcastChannelQualifier
        ) {
            BroadcastChannel(Channel.CONFLATED)
        }

    }

}