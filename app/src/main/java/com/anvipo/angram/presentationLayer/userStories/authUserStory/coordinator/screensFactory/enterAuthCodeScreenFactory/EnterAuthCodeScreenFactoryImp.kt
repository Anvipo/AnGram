package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory

import androidx.fragment.app.Fragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view.EnterAuthCodeFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object EnterAuthCodeScreenFactoryImp : EnterAuthCodeScreenFactory {

    override fun createEnterAuthCodeViewController(
    ): SupportAppScreen = object : SupportAppScreen() {
        override fun getFragment(): Fragment = EnterAuthCodeFragment.createNewInstance() as Fragment
    }

}