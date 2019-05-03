package com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.screensFactory

import com.anvipo.angram.presentationLayer.userStories.mainUserStory.screens.main.view.MainView

interface MainScreensFactory {

    fun createMainScreen(): MainView

}