package com.anvipo.angram.applicationLayer.navigation.coordinator.di

import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorsFactoryImp
import com.anvipo.angram.applicationLayer.types.BackButtonPressedReceiveChannel
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactory
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactoryImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory.EnterAuthCodeScreenFactory
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory.EnterAuthCodeScreenFactoryImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPhoneNumberScreenFactory.EnterPhoneNumberScreenFactory
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPhoneNumberScreenFactory.EnterPhoneNumberScreenFactoryImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.di.EnterAuthCodeModule
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.types.CorrectAuthCodeReceiveChannel
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.di.EnterPhoneNumberModule
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.types.CorrectPhoneNumberReceiveChannel
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ApplicationRootCoordinatorModule {

    internal val applicationCoordinatorsFactory: StringQualifier = named("applicationCoordinatorsFactory")

    private val authorizationScreensFactory: StringQualifier = named("authorizationScreensFactory")

    private val enterPhoneNumberScreenFactory: StringQualifier = named("enterPhoneNumberScreenFactory")
    private val enterAuthCodeScreenFactory: StringQualifier = named("enterAuthCodeScreenFactory")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<ApplicationCoordinatorsFactory>(applicationCoordinatorsFactory) {
            ApplicationCoordinatorsFactoryImp(
                context = get(),
                tdUpdateAuthorizationStateStack = get(),
                authorizationScreensFactory = get<AuthorizationScreensFactory>(authorizationScreensFactory),
                systemMessageNotifier = get()
            )
        }

        single<AuthorizationScreensFactory>(authorizationScreensFactory) {
            AuthorizationScreensFactoryImp(
                enterPhoneNumberScreenFactory = get<EnterPhoneNumberScreenFactory>(enterPhoneNumberScreenFactory),
                enterAuthCodeScreenFactory = get<EnterAuthCodeScreenFactory>(enterAuthCodeScreenFactory)
            )
        }

        single<EnterPhoneNumberScreenFactory>(enterPhoneNumberScreenFactory) {
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

        single<EnterAuthCodeScreenFactory>(enterAuthCodeScreenFactory) {
            EnterAuthCodeScreenFactoryImp(
                enteredCorrectAuthCodeReceiveChannel =
                get<CorrectAuthCodeReceiveChannel>(
                    EnterAuthCodeModule.enteredCorrectAuthCodeReceiveChannel
                ),
                backButtonPressedInAuthCodeScreenReceiveChannel =
                get<BackButtonPressedReceiveChannel>(
                    EnterAuthCodeModule.backButtonPressedInAuthCodeScreenReceiveChannel
                )
            )
        }

    }

}