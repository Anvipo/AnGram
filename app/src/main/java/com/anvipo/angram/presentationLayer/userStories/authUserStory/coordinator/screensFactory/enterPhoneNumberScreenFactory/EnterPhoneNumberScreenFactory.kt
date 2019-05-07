package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPhoneNumberScreenFactory

import com.anvipo.angram.applicationLayer.types.BackButtonPressedReceiveChannel
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.types.CorrectPhoneNumberReceiveChannel
import ru.terrakok.cicerone.android.support.SupportAppScreen

interface EnterPhoneNumberScreenFactory {

    fun createEnterPhoneNumberViewController(
        tdLibGateway: TDLibGateway
    ): Triple<
            SupportAppScreen,
            CorrectPhoneNumberReceiveChannel,
            BackButtonPressedReceiveChannel
            >

}