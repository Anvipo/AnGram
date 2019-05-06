package com.anvipo.angram.applicationLayer.navigation.coordinator.di

import com.anvipo.angram.applicationLayer.di.LaunchSystemModule
import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorsFactoryImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.AuthorizationViewControllersFactory
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.AuthorizationViewControllersFactoryImp
import org.koin.core.module.Module
import org.koin.dsl.module

object ApplicationRootCoordinatorModule {

    val module: Module = module {

        single<ApplicationCoordinatorsFactory> {
            ApplicationCoordinatorsFactoryImp(
                context = get(),
                tdUpdateAuthorizationStateStack = get(),
                authorizationViewControllersFactory = get(),
                systemMessageNotifier = get()
            )
        }

        single<AuthorizationViewControllersFactory> {
            AuthorizationViewControllersFactoryImp(
                enteredCorrectPhoneNumberNotifier = get(LaunchSystemModule.enterCorrectPhoneNumberNotifier),
                enteredCorrectAuthCodeNotifier = get(LaunchSystemModule.enterCorrectAuthCodeNotifier),
                backPressedInPhoneNumberScreenNotifier = get(LaunchSystemModule.backButtonPressedInPhoneNumberScreen),
                backPressedInAuthCodeScreenNotifier = get(LaunchSystemModule.backButtonPressedInAuthCodeScreen)
            )
        }

    }

}