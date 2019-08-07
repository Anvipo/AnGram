package com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.enterPhoneNumberScreenFactory

import androidx.fragment.app.Fragment
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterPhoneNumber.view.EnterPhoneNumberFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object EnterPhoneNumberScreenFactoryImp : EnterPhoneNumberScreenFactory {

    object EnterPhoneNumberScreen : SupportAppScreen() {
        override fun getFragment(): Fragment = EnterPhoneNumberFragment.createNewInstance() as Fragment
    }

    override fun createEnterPhoneNumberScreen(): SupportAppScreen = EnterPhoneNumberScreen

}