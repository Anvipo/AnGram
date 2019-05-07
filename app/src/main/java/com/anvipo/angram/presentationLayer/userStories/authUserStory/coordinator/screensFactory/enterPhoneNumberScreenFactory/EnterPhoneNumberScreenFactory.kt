package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPhoneNumberScreenFactory

import com.anvipo.angram.applicationLayer.di.BackButtonPressedType
import com.anvipo.angram.applicationLayer.di.CorrectPhoneNumberType
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import kotlinx.coroutines.channels.ReceiveChannel
import ru.terrakok.cicerone.android.support.SupportAppScreen

interface EnterPhoneNumberScreenFactory {

    fun createEnterPhoneNumberViewController(
        tdLibGateway: TDLibGateway
    ): Triple<
            SupportAppScreen,
            ReceiveChannel<CorrectPhoneNumberType>,
            ReceiveChannel<BackButtonPressedType>
            >

}