package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPhoneNumberScreenFactory

import androidx.fragment.app.Fragment
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object EnterPhoneNumberScreenFactoryImp : EnterPhoneNumberScreenFactory {

    object EnterPhoneNumberScreen : SupportAppScreen() {
        override fun getFragment(): Fragment = EnterPhoneNumberFragment.createNewInstance() as Fragment
    }

    override fun createEnterPhoneNumberViewController(
        tdLibGateway: AuthorizationTDLibGateway
    ): SupportAppScreen = EnterPhoneNumberScreen

}