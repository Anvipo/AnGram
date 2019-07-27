package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.authorizationScreensFactory

import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.addProxyScreenFactory.AddProxyScreenFactory
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.addProxyScreenFactory.AddProxyScreenFactoryImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory.EnterAuthenticationCodeScreenFactory
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory.EnterAuthenticationCodeScreenFactoryImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPasswordScreenFactory.EnterPasswordScreenFactory
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPasswordScreenFactory.EnterPasswordScreenFactoryImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPhoneNumberScreenFactory.EnterPhoneNumberScreenFactory
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPhoneNumberScreenFactory.EnterPhoneNumberScreenFactoryImp

object AuthorizationScreensFactoryImp : AuthorizationScreensFactory {

    override val enterPhoneNumberScreenFactory: EnterPhoneNumberScreenFactory = EnterPhoneNumberScreenFactoryImp

    override val enterAuthenticationCodeScreenFactory: EnterAuthenticationCodeScreenFactory =
        EnterAuthenticationCodeScreenFactoryImp

    override val enterPasswordScreenFactory: EnterPasswordScreenFactory = EnterPasswordScreenFactoryImp

    override val addProxyScreenFactory: AddProxyScreenFactory = AddProxyScreenFactoryImp

}
