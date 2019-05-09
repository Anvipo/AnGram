package com.anvipo.angram.applicationLayer.types

import com.anvipo.angram.coreLayer.message.SystemMessage
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import org.drinkless.td.libcore.telegram.TdApi


typealias SystemMessageSendChannel = SendChannel<SystemMessage>
typealias SystemMessageReceiveChannel = ReceiveChannel<SystemMessage>
typealias SystemMessageBroadcastChannel = BroadcastChannel<SystemMessage>


typealias UpdateAuthorizationStateList = List<TdApi.UpdateAuthorizationState>
typealias UpdateAuthorizationStateMutableList = MutableList<TdApi.UpdateAuthorizationState>
