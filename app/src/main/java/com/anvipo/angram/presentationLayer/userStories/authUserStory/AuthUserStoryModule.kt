package com.anvipo.angram.presentationLayer.userStories.authUserStory

import com.anvipo.angram.applicationLayer.types.BackButtonPressedSendChannel
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.di.EnterPhoneNumberModule
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenter
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenterImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.types.CorrectPhoneNumberSendChannel
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module


object AuthUserStoryModule {

    internal val enterPhoneNumberPresenter: StringQualifier = named("enterPhoneNumberPresenter")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<EnterPhoneNumberPresenter>(enterPhoneNumberPresenter) {
            EnterPhoneNumberPresenterImp(
                useCase = get(),
                enteredCorrectPhoneNumberSendChannel =
                get<CorrectPhoneNumberSendChannel>(
                    EnterPhoneNumberModule.enteredCorrectPhoneNumberSendChannel
                ),
                backButtonPressedInPhoneNumberScreenSendChannel =
                get<BackButtonPressedSendChannel>(
                    EnterPhoneNumberModule.backButtonPressedInPhoneNumberScreenSendChannel
                )
            )
        }

    }

}