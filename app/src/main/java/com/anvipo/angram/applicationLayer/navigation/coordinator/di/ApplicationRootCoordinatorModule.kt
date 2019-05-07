package com.anvipo.angram.applicationLayer.navigation.coordinator.di

import com.anvipo.angram.applicationLayer.di.CorrectAuthCodeReceiveChannel
import com.anvipo.angram.applicationLayer.di.LaunchSystemModule
import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorsFactoryImp
import com.anvipo.angram.applicationLayer.types.BackButtonPressedReceiveChannel
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactory
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactoryImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory.EnterAuthCodeScreenFactory
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory.EnterAuthCodeScreenFactoryImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPhoneNumberScreenFactory.EnterPhoneNumberScreenFactory
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPhoneNumberScreenFactory.EnterPhoneNumberScreenFactoryImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.di.EnterPhoneNumberModule
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.types.CorrectPhoneNumberReceiveChannel
import org.koin.core.module.Module
import org.koin.dsl.module

object ApplicationRootCoordinatorModule {

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<ApplicationCoordinatorsFactory> {
            ApplicationCoordinatorsFactoryImp(
                context = get(),
                tdUpdateAuthorizationStateStack = get(),
                authorizationScreensFactory = get(),
                systemMessageNotifier = get()
            )
        }

        single<AuthorizationScreensFactory> {
            AuthorizationScreensFactoryImp(
                enterPhoneNumberScreenFactory =
                get<EnterPhoneNumberScreenFactory>(

                ),
                enterAuthCodeScreenFactory =
                get<EnterAuthCodeScreenFactory>(

                )
            )
        }

        single<EnterPhoneNumberScreenFactory> {
            EnterPhoneNumberScreenFactoryImp(
                enteredCorrectPhoneNumberReceiveChannel =
                get<CorrectPhoneNumberReceiveChannel>(
                    EnterPhoneNumberModule.enteredCorrectPhoneNumberReceiveChannel
                ),
                backButtonPressedInPhoneNumberScreenReceiveChannel =
                get<BackButtonPressedReceiveChannel>(
                    EnterPhoneNumberModule.backButtonPressedInPhoneNumberScreenReceiveChannel
                )
            )
        }

        single<EnterAuthCodeScreenFactory> {
            EnterAuthCodeScreenFactoryImp(
                enteredCorrectAuthCodeReceiveChannel =
                get<CorrectAuthCodeReceiveChannel>(
                    LaunchSystemModule.enteredCorrectAuthCodeReceiveChannel
                ),
                backButtonPressedInAuthCodeScreenReceiveChannel =
                get<BackButtonPressedReceiveChannel>(
                    LaunchSystemModule.backButtonPressedInAuthCodeScreenReceiveChannel
                )
            )
        }

    }

}