@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.anvipo.angram.applicationLayer.types

import com.anvipo.angram.coreLayer.message.SystemMessage
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel

typealias SystemMessageSendChannel = SendChannel<SystemMessage>
typealias SystemMessageReceiveChannel = ReceiveChannel<SystemMessage>
typealias SystemMessageBroadcastChannel = BroadcastChannel<SystemMessage>

typealias ConnectionStateSendChannel = SendChannel<ConnectionState>
typealias ConnectionStateReceiveChannel = ReceiveChannel<ConnectionState>
typealias ConnectionStateBroadcastChannel = BroadcastChannel<ConnectionState>
