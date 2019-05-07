package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPhoneNumberScreenFactory

import androidx.fragment.app.Fragment
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object EnterPhoneNumberScreenFactoryImp : EnterPhoneNumberScreenFactory {

    override fun createEnterPhoneNumberViewController(
        tdLibGateway: TDLibGateway
    ): SupportAppScreen = object : SupportAppScreen() {
        override fun getFragment(): Fragment = EnterPhoneNumberFragment.createNewInstance() as Fragment
    }

}