package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.di

import com.anvipo.angram.applicationLayer.types.BackButtonPressedBroadcastChannel
import com.anvipo.angram.applicationLayer.types.BackButtonPressedReceiveChannel
import com.anvipo.angram.applicationLayer.types.BackButtonPressedSendChannel
import com.anvipo.angram.applicationLayer.types.BackButtonPressedType
import com.anvipo.angram.businessLogicLayer.di.UseCasesModule.enterPhoneNumberUseCase
import com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase.EnterPhoneNumberUseCase
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenter
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenterImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.types.CorrectPhoneNumberBroadcastChannel
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.types.CorrectPhoneNumberReceiveChannel
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.types.CorrectPhoneNumberSendChannel
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.types.CorrectPhoneNumberType
import kotlinx.coroutines.channels.BroadcastChannel
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object EnterPhoneNumberModule {

    private val enteredCorrectPhoneNumberBroadcastChannel: StringQualifier =
        named("enteredCorrectPhoneNumberBroadcastChannel")
    internal val enteredCorrectPhoneNumberReceiveChannel: StringQualifier =
        named("enteredCorrectPhoneNumberReceiveChannel")
    internal val enteredCorrectPhoneNumberSendChannel: StringQualifier =
        named("enteredCorrectPhoneNumberSendChannel")

    private val backButtonPressedInPhoneNumberScreenBroadcastChannel: StringQualifier =
        named("backButtonPressedInPhoneNumberScreenBroadcastChannel")
    internal val backButtonPressedInPhoneNumberScreenReceiveChannel: StringQualifier =
        named("backButtonPressedInPhoneNumberScreenReceiveChannel")
    internal val backButtonPressedInPhoneNumberScreenSendChannel: StringQualifier =
        named("backButtonPressedInPhoneNumberScreenSendChannel")


    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<CorrectPhoneNumberBroadcastChannel>(enteredCorrectPhoneNumberBroadcastChannel) {
            BroadcastChannel<CorrectPhoneNumberType>(1)
        }
        single<CorrectPhoneNumberReceiveChannel>(enteredCorrectPhoneNumberReceiveChannel) {
            get<CorrectPhoneNumberBroadcastChannel>(
                enteredCorrectPhoneNumberBroadcastChannel
            ).openSubscription()
        }
        single<CorrectPhoneNumberSendChannel>(enteredCorrectPhoneNumberSendChannel) {
            get<CorrectPhoneNumberBroadcastChannel>(enteredCorrectPhoneNumberBroadcastChannel)
        }

        single<BackButtonPressedBroadcastChannel>(backButtonPressedInPhoneNumberScreenBroadcastChannel) {
            BroadcastChannel<BackButtonPressedType>(1)
        }
        single<BackButtonPressedReceiveChannel>(backButtonPressedInPhoneNumberScreenReceiveChannel) {
            get<BackButtonPressedBroadcastChannel>(
                backButtonPressedInPhoneNumberScreenBroadcastChannel
            ).openSubscription()
        }
        single<BackButtonPressedSendChannel>(backButtonPressedInPhoneNumberScreenSendChannel) {
            get<BackButtonPressedBroadcastChannel>(backButtonPressedInPhoneNumberScreenBroadcastChannel)
        }


        single<EnterPhoneNumberPresenterImp>(enterPhoneNumberPresenter) {
            EnterPhoneNumberPresenterImp(
                useCase = get<EnterPhoneNumberUseCase>(enterPhoneNumberUseCase),
                enteredCorrectPhoneNumberSendChannel =
                get<CorrectPhoneNumberSendChannel>(enteredCorrectPhoneNumberSendChannel),
                backButtonPressedInPhoneNumberScreenSendChannel =
                get<BackButtonPressedSendChannel>(backButtonPressedInPhoneNumberScreenSendChannel)
            )
        }

    }

    internal val enterPhoneNumberPresenter: StringQualifier = named("enterPhoneNumberPresenter")

}
