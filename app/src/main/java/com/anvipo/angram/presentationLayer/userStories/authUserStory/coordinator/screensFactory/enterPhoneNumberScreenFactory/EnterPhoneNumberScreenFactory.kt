package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPhoneNumberScreenFactory

import com.anvipo.angram.dataLayer.gateways.tdLibGateway.authorization.AuthorizationTDLibGateway
import ru.terrakok.cicerone.android.support.SupportAppScreen

interface EnterPhoneNumberScreenFactory {

    fun createEnterPhoneNumberViewController(tdLibGateway: AuthorizationTDLibGateway): SupportAppScreen

}