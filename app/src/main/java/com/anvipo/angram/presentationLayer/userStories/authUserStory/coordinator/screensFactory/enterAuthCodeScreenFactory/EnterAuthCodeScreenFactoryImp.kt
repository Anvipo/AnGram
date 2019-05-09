package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory

import androidx.fragment.app.Fragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view.EnterAuthCodeFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object EnterAuthCodeScreenFactoryImp : EnterAuthCodeScreenFactory {

    data class EnterAuthCodeScreen(val shouldShowBackButton: Boolean) : SupportAppScreen() {
        override fun getFragment(): Fragment =
            EnterAuthCodeFragment.createNewInstance(shouldShowBackButton) as Fragment
    }

    override fun createEnterAuthCodeViewController(showBackButton: Boolean): SupportAppScreen =
        EnterAuthCodeScreen(shouldShowBackButton = showBackButton)

}