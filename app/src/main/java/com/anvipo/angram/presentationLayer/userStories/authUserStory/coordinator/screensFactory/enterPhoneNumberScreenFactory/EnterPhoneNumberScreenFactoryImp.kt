package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPhoneNumberScreenFactory

import androidx.fragment.app.Fragment
import com.anvipo.angram.applicationLayer.di.BackButtonPressedType
import com.anvipo.angram.applicationLayer.di.CorrectPhoneNumberType
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberFragment
import kotlinx.coroutines.channels.ReceiveChannel
import ru.terrakok.cicerone.android.support.SupportAppScreen

class EnterPhoneNumberScreenFactoryImp(
    private val enteredCorrectPhoneNumberReceiveChannel: ReceiveChannel<CorrectPhoneNumberType>,
    private val backButtonPressedInPhoneNumberScreenReceiveChannel: ReceiveChannel<BackButtonPressedType>
) : EnterPhoneNumberScreenFactory {

    override fun createEnterPhoneNumberViewController(
        tdLibGateway: TDLibGateway
    ): Triple<SupportAppScreen, ReceiveChannel<CorrectPhoneNumberType>, ReceiveChannel<BackButtonPressedType>> {
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