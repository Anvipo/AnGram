package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPhoneNumberScreenFactory

import com.anvipo.angram.dataLayer.gateways.tdLibGateway.TDLibGateway
import ru.terrakok.cicerone.android.support.SupportAppScreen

interface EnterPhoneNumberScreenFactory {

    fun createEnterPhoneNumberViewController(tdLibGateway: TDLibGateway): SupportAppScreen

}