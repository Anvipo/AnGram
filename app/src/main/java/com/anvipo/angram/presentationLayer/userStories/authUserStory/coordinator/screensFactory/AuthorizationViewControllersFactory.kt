package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory

import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberView

interface AuthorizationViewControllersFactory {

    fun createEnterPhoneNumberViewController(tdLibGateway: TDLibGateway): EnterPhoneNumberView

}