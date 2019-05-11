package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPasswordScreenFactory

import androidx.fragment.app.Fragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationPassword.view.EnterAuthenticationPasswordFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object EnterPasswordScreenFactoryImp : EnterPasswordScreenFactory {

    object EnterPhoneNumberScreen : SupportAppScreen() {
        override fun getFragment(): Fragment = EnterAuthenticationPasswordFragment.createNewInstance() as Fragment
    }

    override fun createEnterPasswordViewController(): SupportAppScreen = EnterPhoneNumberScreen

}