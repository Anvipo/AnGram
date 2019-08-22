package com.anvipo.angram.layers.global.types

import com.anvipo.angram.layers.core.collections.stack.IMutableStack
import com.anvipo.angram.layers.core.collections.stack.IReadOnlyStack
import com.anvipo.angram.layers.core.collections.stack.MyStack
import com.anvipo.angram.layers.core.message.SystemMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import org.drinkless.td.libcore.telegram.TdApi

typealias EnabledProxyIdSendChannel = SendChannel<Int?>
typealias EnabledProxyIdReceiveChannel = ReceiveChannel<Int?>
@UseExperimental(ExperimentalCoroutinesApi::class)
typealias EnabledProxyIdBroadcastChannel = BroadcastChannel<Int?>

typealias SystemMessageSendChannel = SendChannel<SystemMessage>
typealias SystemMessageReceiveChannel = ReceiveChannel<SystemMessage>
@UseExperimental(ExperimentalCoroutinesApi::class)
typealias SystemMessageBroadcastChannel = BroadcastChannel<SystemMessage>

typealias TdApiUpdateAuthorizationStateSendChannel = SendChannel<TdApi.UpdateAuthorizationState>
typealias TdApiUpdateAuthorizationStateReceiveChannel = ReceiveChannel<TdApi.UpdateAuthorizationState>
@UseExperimental(ExperimentalCoroutinesApi::class)
typealias TdApiUpdateAuthorizationStateBroadcastChannel = BroadcastChannel<TdApi.UpdateAuthorizationState>

typealias TdApiUpdateConnectionStateSendChannel = SendChannel<TdApi.UpdateConnectionState>
typealias TdApiUpdateConnectionStateReceiveChannel = ReceiveChannel<TdApi.UpdateConnectionState>
@UseExperimental(ExperimentalCoroutinesApi::class)
typealias TdApiUpdateConnectionStateBroadcastChannel = BroadcastChannel<TdApi.UpdateConnectionState>

typealias TDLibUpdatesException = Throwable
typealias TDLibDefaultException = Throwable
