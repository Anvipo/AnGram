package com.anvipo.angram.applicationLayer.navigation.coordinator.di

import com.anvipo.angram.applicationLayer.di.LaunchSystemModule
import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorsFactoryImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactory
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactoryImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory.EnterAuthCodeScreenFactory
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory.EnterAuthCodeScreenFactoryImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPhoneNumberScreenFactory.EnterPhoneNumberScreenFactory
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPhoneNumberScreenFactory.EnterPhoneNumberScreenFactoryImp
import org.koin.core.module.Module
import org.koin.dsl.module

object ApplicationRootCoordinatorModule {

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
                enterPhoneNumberScreenFactory = get(),
                enterAuthCodeScreenFactory = get()
            )
        }

        single<EnterPhoneNumberScreenFactory> {
            EnterPhoneNumberScreenFactoryImp(
                enteredCorrectPhoneNumberReceiveChannel = get(LaunchSystemModule.enteredCorrectPhoneNumberReceiveChannel),
                backButtonPressedInPhoneNumberScreenReceiveChannel = get(LaunchSystemModule.backButtonPressedInPhoneNumberScreenReceiveChannel)
            )
        }

        single<EnterAuthCodeScreenFactory> {
            EnterAuthCodeScreenFactoryImp(
                enteredCorrectAuthCodeReceiveChannel = get(LaunchSystemModule.enteredCorrectAuthCodeReceiveChannel),
                backButtonPressedInAuthCodeScreenReceiveChannel = get(LaunchSystemModule.backButtonPressedInAuthCodeScreenReceiveChannel)
            )
        }

    }

}