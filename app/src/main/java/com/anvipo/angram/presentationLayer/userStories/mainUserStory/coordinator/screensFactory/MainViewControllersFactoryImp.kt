package com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.screensFactory

import androidx.fragment.app.Fragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberFragment
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.screens.main.view.MainView
import ru.terrakok.cicerone.android.support.SupportAppScreen

object MainViewControllersFactoryImp : MainScreensFactory {

    override fun createMainScreen(): MainView = object : SupportAppScreen() {
        override fun getFragment(): Fragment = EnterPhoneNumberFragment.createNewInstance() as Fragment
    }.fragment as MainView

}