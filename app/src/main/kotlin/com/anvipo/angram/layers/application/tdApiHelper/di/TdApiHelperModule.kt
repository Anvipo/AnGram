package com.anvipo.angram.layers.application.tdApiHelper.di

import com.anvipo.angram.layers.application.launchSystem.appActivity.types.TDLibClientHasBeenRecreatedBroadcastChannel
import com.anvipo.angram.layers.application.launchSystem.appActivity.types.TDLibClientHasBeenRecreatedSendChannel
import com.anvipo.angram.layers.global.OrderedChat
import com.anvipo.angram.layers.global.types.EnabledProxyIdBroadcastChannel
import com.anvipo.angram.layers.global.types.EnabledProxyIdSendChannel
import com.anvipo.angram.layers.global.types.SystemMessageBroadcastChannel
import com.anvipo.angram.layers.global.types.SystemMessageSendChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object TdApiHelperModule {

    val enabledProxyIdSendChannelQualifier: StringQualifier =
        named("enabledProxyIdSendChannel")
    val enabledProxyIdReceiveChannelQualifier: StringQualifier =
        named("enabledProxyIdReceiveChannel")
    private val enabledProxyIdBroadcastChannelQualifier =
        named("enabledProxyIdBroadcastChannel")

    val systemMessageSendChannelQualifier: StringQualifier =
        named("systemMessageSendChannel")
    val systemMessageReceiveChannelQualifier: StringQualifier =
        named("systemMessageReceiveChannel")
    private val systemMessageBroadcastChannelQualifier =
        named("systemMessageBroadcastChannel")

    val tdLibClientHasBeenRecreatedReceiveChannelQualifier: StringQualifier =
        named("tdLibClientHasBeenRecreatedReceiveChannel")
    val tdLibClientHasBeenRecreatedSendChannelQualifier: StringQualifier =
        named("tdLibClientHasBeenRecreatedSendChannel")
    private val tdLibClientHasBeenRecreatedBroadcastChannelQualifier =
        named("tdLibClientHasBeenRecreatedBroadcastChannel")

    val usersMutableMapQualifier: StringQualifier = named("usersMutableMap")
    val basicGroupsMutableMapQualifier: StringQualifier = named("basicGroupsMutableMap")
    val superGroupsMutableMapQualifier: StringQualifier = named("superGroupsMutableMap")
    val secretChatsMutableMapQualifier: StringQualifier = named("secretChatsMutableMap")
    val chatsMutableMapQualifier: StringQualifier = named("chatsMutableMap")
    val chatListQualifier: StringQualifier = named("chatListMutableMap")
    val usersFullInfoMutableMapQualifier: StringQualifier = named("usersFullInfoMutableMap")
    val basicGroupsFullInfoMutableMapQualifier: StringQualifier =
        named("basicGroupsFullInfoMutableMap")
    val supergroupsFullInfoMutableMapQualifier: StringQualifier =
        named("supergroupsFullInfoMutableMap")

    @ExperimentalCoroutinesApi
    val module: Module = module {

        single<MutableMap<Int, TdApi.User>>(usersMutableMapQualifier) {
            ConcurrentHashMap()
        }

        single<MutableMap<Int, TdApi.BasicGroup>>(basicGroupsMutableMapQualifier) {
            ConcurrentHashMap()
        }

        single<MutableMap<Int, TdApi.Supergroup>>(superGroupsMutableMapQualifier) {
            ConcurrentHashMap()
        }

        single<MutableMap<Int, TdApi.SecretChat>>(secretChatsMutableMapQualifier) {
            ConcurrentHashMap()
        }

        single<MutableMap<Long, TdApi.Chat>>(chatsMutableMapQualifier) {
            ConcurrentHashMap()
        }

        single<MutableSet<OrderedChat>>(chatListQualifier) {
            TreeSet()
        }

        single<MutableMap<Int, TdApi.UserFullInfo>>(usersFullInfoMutableMapQualifier) {
            ConcurrentHashMap()
        }

        single<MutableMap<Int, TdApi.BasicGroupFullInfo>>(
            basicGroupsFullInfoMutableMapQualifier
        ) {
            ConcurrentHashMap()
        }

        single<MutableMap<Int, TdApi.SupergroupFullInfo>>(
            supergroupsFullInfoMutableMapQualifier
        ) {
            ConcurrentHashMap()
        }


        single<EnabledProxyIdSendChannel>(enabledProxyIdSendChannelQualifier) {
            get(enabledProxyIdBroadcastChannelQualifier)
        }
        factory(enabledProxyIdReceiveChannelQualifier) {
            get<EnabledProxyIdBroadcastChannel>(enabledProxyIdBroadcastChannelQualifier)
                .openSubscription()
        }
        single<EnabledProxyIdBroadcastChannel>(enabledProxyIdBroadcastChannelQualifier) {
            BroadcastChannel(Channel.CONFLATED)
        }


        single<SystemMessageSendChannel>(systemMessageSendChannelQualifier) {
            get(systemMessageBroadcastChannelQualifier)
        }
        factory(systemMessageReceiveChannelQualifier) {
            get<SystemMessageBroadcastChannel>(systemMessageBroadcastChannelQualifier)
                .openSubscription()
        }
        single<SystemMessageBroadcastChannel>(systemMessageBroadcastChannelQualifier) {
            BroadcastChannel(Channel.CONFLATED)
        }


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