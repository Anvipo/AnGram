@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.anvipo.angram.layers.global.types

import com.anvipo.angram.layers.core.message.SystemMessage
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import org.drinkless.td.libcore.telegram.TdApi

typealias TDLibClientHasBeenRecreated = Unit
typealias TDLibClientHasBeenRecreatedSendChannel = SendChannel<TDLibClientHasBeenRecreated>
typealias TDLibClientHasBeenRecreatedReceiveChannel = ReceiveChannel<TDLibClientHasBeenRecreated>
typealias TDLibClientHasBeenRecreatedBroadcastChannel = BroadcastChannel<TDLibClientHasBeenRecreated>

typealias MustRecreateTDLibClient = Unit
typealias MustRecreateTDLibClientSendChannel = SendChannel<MustRecreateTDLibClient>
typealias MustRecreateTDLibClientReceiveChannel = ReceiveChannel<MustRecreateTDLibClient>
typealias MustRecreateTDLibClientBroadcastChannel = BroadcastChannel<MustRecreateTDLibClient>

typealias EnabledProxyIdSendChannel = SendChannel<Int?>
typealias EnabledProxyIdReceiveChannel = ReceiveChannel<Int?>
typealias EnabledProxyIdBroadcastChannel = BroadcastChannel<Int?>

typealias SystemMessageSendChannel = SendChannel<SystemMessage>
typealias SystemMessageReceiveChannel = ReceiveChannel<SystemMessage>
typealias SystemMessageBroadcastChannel = BroadcastChannel<SystemMessage>

typealias TdApiUpdateAuthorizationStateSendChannel = SendChannel<TdApiUpdateAuthorizationState>
typealias TdApiUpdateAuthorizationStateReceiveChannel = ReceiveChannel<TdApiUpdateAuthorizationState>
typealias TdApiUpdateAuthorizationStateBroadcastChannel = BroadcastChannel<TdApiUpdateAuthorizationState>

typealias TdApiUpdateConnectionStateSendChannel = SendChannel<TdApiUpdateConnectionState>
typealias TdApiUpdateConnectionStateReceiveChannel = ReceiveChannel<TdApiUpdateConnectionState>
typealias TdApiUpdateConnectionStateBroadcastChannel = BroadcastChannel<TdApiUpdateConnectionState>

typealias TdApiUpdate = TdApi.Update
typealias TdApiUpdateOption = TdApi.UpdateOption
typealias TdApiUpdateAuthorizationState = TdApi.UpdateAuthorizationState
typealias TdApiUpdateConnectionState = TdApi.UpdateConnectionState

typealias TdApiObject = TdApi.Object

typealias TDLibUpdatesException = Throwable
typealias TDLibDefaultException = Throwable
