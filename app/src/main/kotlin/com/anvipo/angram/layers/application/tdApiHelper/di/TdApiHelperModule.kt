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

    val enabledProxyIdSendChannelQualifier: StringQualifier = named("enabledProxyIdSendChannel")
    val enabledProxyIdReceiveChannelQualifier: StringQualifier = named("enabledProxyIdReceiveChannel")
    private val enabledProxyIdBroadcastChannelQualifier = named("enabledProxyIdBroadcastChannel")

    val systemMessageSendChannelQualifier: StringQualifier = named("systemMessageSendChannel")
    val systemMessageReceiveChannelQualifier: StringQualifier = named("systemMessageReceiveChannel")
    private val systemMessageBroadcastChannelQualifier = named("systemMessageBroadcastChannel")

    val tdLibClientHasBeenRecreatedReceiveChannelQualifier: StringQualifier =
        named("tdLibClientHasBeenRecreatedReceiveChannel")
    val tdLibClientHasBeenRecreatedSendChannelQualifier: StringQualifier =
        named("tdLibClientHasBeenRecreatedSendChannel")
    private val tdLibClientHasBeenRecreatedBroadcastChannelQualifier =
        named("tdLibClientHasBeenRecreatedBroadcastChannel")

    val usersQualifier: StringQualifier = named("users")
    val basicGroupsQualifier: StringQualifier = named("basicGroups")
    val superGroupsQualifier: StringQualifier = named("superGroups")
    val secretChatsQualifier: StringQualifier = named("secretChats")
    val chatsQualifier: StringQualifier = named("chats")
    val chatListQualifier: StringQualifier = named("chatList")
    val usersFullInfoQualifier: StringQualifier = named("usersFullInfo")
    val basicGroupsFullInfoQualifier: StringQualifier = named("basicGroupsFullInfo")
    val supergroupsFullInfoQualifier: StringQualifier = named("supergroupsFullInfo")

    @ExperimentalCoroutinesApi
    val module: Module = module {

        single(usersQualifier) {
            ConcurrentHashMap<Int, TdApi.User>()
        }

        single(basicGroupsQualifier) {
            ConcurrentHashMap<Int, TdApi.BasicGroup>()
        }

        single(superGroupsQualifier) {
            ConcurrentHashMap<Int, TdApi.Supergroup>()
        }

        single(secretChatsQualifier) {
            ConcurrentHashMap<Int, TdApi.SecretChat>()
        }

        single(chatsQualifier) {
            ConcurrentHashMap<Long, TdApi.Chat>()
        }

        single(chatListQualifier) {
            TreeSet<OrderedChat>()
        }

        single(usersFullInfoQualifier) {
            ConcurrentHashMap<Int, TdApi.UserFullInfo>()
        }

        single(basicGroupsFullInfoQualifier) {
            ConcurrentHashMap<Int, TdApi.BasicGroupFullInfo>()
        }

        single(supergroupsFullInfoQualifier) {
            ConcurrentHashMap<Int, TdApi.SupergroupFullInfo>()
        }


        single<EnabledProxyIdSendChannel>(enabledProxyIdSendChannelQualifier) {
            get(enabledProxyIdBroadcastChannelQualifier)
        }
        factory(enabledProxyIdReceiveChannelQualifier) {
            get<EnabledProxyIdBroadcastChannel>(enabledProxyIdBroadcastChannelQualifier).openSubscription()
        }
        single<EnabledProxyIdBroadcastChannel>(enabledProxyIdBroadcastChannelQualifier) {
            BroadcastChannel(Channel.CONFLATED)
        }


        single<SystemMessageSendChannel>(systemMessageSendChannelQualifier) {
            get(systemMessageBroadcastChannelQualifier)
        }
        factory(systemMessageReceiveChannelQualifier) {
            get<SystemMessageBroadcastChannel>(systemMessageBroadcastChannelQualifier).openSubscription()
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