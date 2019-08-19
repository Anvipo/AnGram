package com.anvipo.angram.layers.application.launchSystem.appActivity.types

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel

typealias TDLibClientHasBeenRecreatedSendChannel = SendChannel<Unit>
typealias TDLibClientHasBeenRecreatedReceiveChannel = ReceiveChannel<Unit>
@UseExperimental(ExperimentalCoroutinesApi::class)
typealias TDLibClientHasBeenRecreatedBroadcastChannel = BroadcastChannel<Unit>