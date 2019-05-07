package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPhoneNumberScreenFactory

import androidx.fragment.app.Fragment
import com.anvipo.angram.applicationLayer.types.BackButtonPressedReceiveChannel
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.types.CorrectPhoneNumberReceiveChannel
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class EnterPhoneNumberScreenFactoryImp(
    private val enteredCorrectPhoneNumberReceiveChannel: CorrectPhoneNumberReceiveChannel,
    private val backButtonPressedInPhoneNumberScreenReceiveChannel: BackButtonPressedReceiveChannel
) : EnterPhoneNumberScreenFactory {

    override fun createEnterPhoneNumberViewController(
        tdLibGateway: TDLibGateway
    ): Triple<SupportAppScreen, CorrectPhoneNumberReceiveChannel, BackButtonPressedReceiveChannel> {
        val enterPhoneNumberScreen = object : SupportAppScreen() {
            override fun getFragment(): Fragment = EnterPhoneNumberFragment.createNewInstance() as Fragment
        }

        return Triple(
            enterPhoneNumberScreen,
            enteredCorrectPhoneNumberReceiveChannel,
            backButtonPressedInPhoneNumberScreenReceiveChannel
        )
    }

}