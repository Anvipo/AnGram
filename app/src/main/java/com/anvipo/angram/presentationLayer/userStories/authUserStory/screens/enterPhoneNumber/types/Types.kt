package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.types

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel

typealias CorrectPhoneNumberType = String

typealias CorrectPhoneNumberBroadcastChannel = BroadcastChannel<CorrectPhoneNumberType>
typealias CorrectPhoneNumberReceiveChannel = ReceiveChannel<CorrectPhoneNumberType>
typealias CorrectPhoneNumberSendChannel = SendChannel<CorrectPhoneNumberType>