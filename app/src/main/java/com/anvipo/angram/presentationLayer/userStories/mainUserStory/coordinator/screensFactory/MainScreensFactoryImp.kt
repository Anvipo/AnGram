package com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.screensFactory

import com.anvipo.angram.presentationLayer.userStories.mainUserStory.screens.main.view.MainFragment
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.screens.main.view.MainView

class MainScreensFactoryImp : MainScreensFactory {

    override fun createMainScreen(): MainView {
        return MainFragment.createNewInstance()
    }

}