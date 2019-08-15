package com.anvipo.angram.layers.global.types

import com.anvipo.angram.layers.core.message.SystemMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import org.drinkless.td.libcore.telegram.TdApi

typealias TDLibClientHasBeenRecreated = Unit
typealias TDLibClientHasBeenRecreatedSendChannel = SendChannel<TDLibClientHasBeenRecreated>
typealias TDLibClientHasBeenRecreatedReceiveChannel = ReceiveChannel<TDLibClientHasBeenRecreated>
@UseExperimental(ExperimentalCoroutinesApi::class)
typealias TDLibClientHasBeenRecreatedBroadcastChannel = BroadcastChannel<TDLibClientHasBeenRecreated>

typealias MustRecreateTDLibClient = Unit
typealias MustRecreateTDLibClientSendChannel = SendChannel<MustRecreateTDLibClient>
typealias MustRecreateTDLibClientReceiveChannel = ReceiveChannel<MustRecreateTDLibClient>
@UseExperimental(ExperimentalCoroutinesApi::class)
typealias MustRecreateTDLibClientBroadcastChannel = BroadcastChannel<MustRecreateTDLibClient>

typealias EnabledProxyIdSendChannel = SendChannel<Int?>
typealias EnabledProxyIdReceiveChannel = ReceiveChannel<Int?>
@UseExperimental(ExperimentalCoroutinesApi::class)
typealias EnabledProxyIdBroadcastChannel = BroadcastChannel<Int?>

typealias SystemMessageSendChannel = SendChannel<SystemMessage>
typealias SystemMessageReceiveChannel = ReceiveChannel<SystemMessage>
@UseExperimental(ExperimentalCoroutinesApi::class)
typealias SystemMessageBroadcastChannel = BroadcastChannel<SystemMessage>

typealias TdApiUpdateAuthorizationStateSendChannel = SendChannel<TdApiUpdateAuthorizationState>
typealias TdApiUpdateAuthorizationStateReceiveChannel = ReceiveChannel<TdApiUpdateAuthorizationState>
@UseExperimental(ExperimentalCoroutinesApi::class)
typealias TdApiUpdateAuthorizationStateBroadcastChannel = BroadcastChannel<TdApiUpdateAuthorizationState>

typealias TdApiUpdateConnectionStateSendChannel = SendChannel<TdApiUpdateConnectionState>
typealias TdApiUpdateConnectionStateReceiveChannel = ReceiveChannel<TdApiUpdateConnectionState>
@UseExperimental(ExperimentalCoroutinesApi::class)
typealias TdApiUpdateConnectionStateBroadcastChannel = BroadcastChannel<TdApiUpdateConnectionState>

typealias TdApiUpdate = TdApi.Update
typealias TdApiUpdateOption = TdApi.UpdateOption
typealias TdApiUpdateAuthorizationState = TdApi.UpdateAuthorizationState
typealias TdApiUpdateConnectionState = TdApi.UpdateConnectionState

typealias TdApiObject = TdApi.Object

typealias TDLibUpdatesException = Throwable
typealias TDLibDefaultException = Throwable
