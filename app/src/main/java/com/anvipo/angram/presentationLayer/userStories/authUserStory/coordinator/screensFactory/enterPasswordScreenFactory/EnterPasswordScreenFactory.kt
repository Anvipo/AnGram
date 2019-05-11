package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPasswordScreenFactory

import ru.terrakok.cicerone.android.support.SupportAppScreen

interface EnterPasswordScreenFactory {
    fun createEnterPasswordViewController(): SupportAppScreen
}