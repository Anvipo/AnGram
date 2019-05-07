package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory

import com.anvipo.angram.applicationLayer.di.BackButtonPressedType
import com.anvipo.angram.applicationLayer.di.CorrectAuthCodeType
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import kotlinx.coroutines.channels.ReceiveChannel
import ru.terrakok.cicerone.android.support.SupportAppScreen

interface EnterAuthCodeScreenFactory {

    fun createEnterAuthCodeViewController(
        tdLibGateway: TDLibGateway
    ): Triple<
            SupportAppScreen,
            ReceiveChannel<CorrectAuthCodeType>,
            ReceiveChannel<BackButtonPressedType>
            >

}