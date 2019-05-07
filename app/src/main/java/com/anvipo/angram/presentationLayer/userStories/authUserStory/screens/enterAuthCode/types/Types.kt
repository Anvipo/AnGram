package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.types

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel

typealias CorrectAuthCodeType = String

typealias CorrectAuthCodeBroadcastChannel = BroadcastChannel<CorrectAuthCodeType>
typealias CorrectAuthCodeReceiveChannel = ReceiveChannel<CorrectAuthCodeType>
typealias CorrectAuthCodeSendChannel = SendChannel<CorrectAuthCodeType>