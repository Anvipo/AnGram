package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.authorizationScreensFactory

import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory.EnterAuthCodeScreenFactory
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory.EnterAuthCodeScreenFactoryImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPasswordScreenFactory.EnterPasswordScreenFactory
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPasswordScreenFactory.EnterPasswordScreenFactoryImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPhoneNumberScreenFactory.EnterPhoneNumberScreenFactory
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPhoneNumberScreenFactory.EnterPhoneNumberScreenFactoryImp

object AuthorizationScreensFactoryImp : AuthorizationScreensFactory {

    override val enterPhoneNumberScreenFactory: EnterPhoneNumberScreenFactory = EnterPhoneNumberScreenFactoryImp

    override val enterAuthCodeScreenFactory: EnterAuthCodeScreenFactory = EnterAuthCodeScreenFactoryImp

    override val enterPasswordScreenFactory: EnterPasswordScreenFactory = EnterPasswordScreenFactoryImp

}
