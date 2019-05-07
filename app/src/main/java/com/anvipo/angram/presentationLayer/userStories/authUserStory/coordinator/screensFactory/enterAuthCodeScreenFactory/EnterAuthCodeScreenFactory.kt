package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory

import com.anvipo.angram.applicationLayer.types.BackButtonPressedReceiveChannel
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.types.CorrectAuthCodeReceiveChannel
import ru.terrakok.cicerone.android.support.SupportAppScreen

interface EnterAuthCodeScreenFactory {

    fun createEnterAuthCodeViewController(
        tdLibGateway: TDLibGateway
    ): Triple<
            SupportAppScreen,
            CorrectAuthCodeReceiveChannel,
            BackButtonPressedReceiveChannel
            >

}