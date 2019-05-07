package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.di

import com.anvipo.angram.applicationLayer.types.BackButtonPressedBroadcastChannel
import com.anvipo.angram.applicationLayer.types.BackButtonPressedReceiveChannel
import com.anvipo.angram.applicationLayer.types.BackButtonPressedSendChannel
import com.anvipo.angram.applicationLayer.types.BackButtonPressedType
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.types.CorrectAuthCodeBroadcastChannel
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.types.CorrectAuthCodeReceiveChannel
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.types.CorrectAuthCodeSendChannel
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.types.CorrectAuthCodeType
import kotlinx.coroutines.channels.BroadcastChannel
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object EnterAuthCodeModule {

    private val enteredCorrectAuthCodeBroadcastChannel: StringQualifier =
        named("enteredCorrectAuthCodeBroadcastChannel")
    internal val enteredCorrectAuthCodeReceiveChannel: StringQualifier =
        named("enteredCorrectAuthCodeReceiveChannel")
    internal val enteredCorrectAuthCodeSendChannel: StringQualifier =
        named("enteredCorrectAuthCodeSendChannel")

    private val backButtonPressedInAuthCodeScreenBroadcastChannel: StringQualifier =
        named("backButtonPressedInAuthCodeScreenBroadcastChannel")
    internal val backButtonPressedInAuthCodeScreenReceiveChannel: StringQualifier =
        named("backButtonPressedInAuthCodeScreenReceiveChannel")
    private val backButtonPressedInAuthCodeScreenSendChannel: StringQualifier =
        named("backButtonPressedInAuthCodeScreenSendChannel")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<CorrectAuthCodeBroadcastChannel>(enteredCorrectAuthCodeBroadcastChannel) {
            BroadcastChannel<CorrectAuthCodeType>(1)
        }
        single<CorrectAuthCodeReceiveChannel>(enteredCorrectAuthCodeReceiveChannel) {
            get<CorrectAuthCodeBroadcastChannel>(
                enteredCorrectAuthCodeBroadcastChannel
            ).openSubscription()
        }
        single<CorrectAuthCodeSendChannel>(enteredCorrectAuthCodeSendChannel) {
            get<CorrectAuthCodeBroadcastChannel>(enteredCorrectAuthCodeBroadcastChannel)
        }

        single<BackButtonPressedBroadcastChannel>(backButtonPressedInAuthCodeScreenBroadcastChannel) {
            BroadcastChannel<BackButtonPressedType>(1)
        }
        single<BackButtonPressedReceiveChannel>(backButtonPressedInAuthCodeScreenReceiveChannel) {
            get<BackButtonPressedBroadcastChannel>(
                backButtonPressedInAuthCodeScreenBroadcastChannel
            ).openSubscription()
        }
        single<BackButtonPressedSendChannel>(backButtonPressedInAuthCodeScreenSendChannel) {
            get<BackButtonPressedBroadcastChannel>(backButtonPressedInAuthCodeScreenBroadcastChannel)
        }

    }

}