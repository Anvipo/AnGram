package com.anvipo.angram.applicationLayer.types

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel

typealias BackButtonPressedType = Unit

typealias BackButtonPressedBroadcastChannel = BroadcastChannel<BackButtonPressedType>
typealias BackButtonPressedReceiveChannel = ReceiveChannel<BackButtonPressedType>
typealias BackButtonPressedSendChannel = SendChannel<BackButtonPressedType>