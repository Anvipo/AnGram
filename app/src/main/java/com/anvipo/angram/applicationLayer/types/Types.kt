package com.anvipo.angram.applicationLayer.types

import com.anvipo.angram.coreLayer.collections.IMutableStack
import com.anvipo.angram.coreLayer.collections.IReadOnlyStack
import com.anvipo.angram.coreLayer.message.SystemMessage
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import org.drinkless.td.libcore.telegram.TdApi


typealias SystemMessageSendChannel = SendChannel<SystemMessage>
typealias SystemMessageReceiveChannel = ReceiveChannel<SystemMessage>
typealias SystemMessageBroadcastChannel = BroadcastChannel<SystemMessage>


typealias UpdateAuthorizationStateIMutableStack = IMutableStack<TdApi.UpdateAuthorizationState>
typealias UpdateAuthorizationStateIReadOnlyStack = IReadOnlyStack<TdApi.UpdateAuthorizationState>
