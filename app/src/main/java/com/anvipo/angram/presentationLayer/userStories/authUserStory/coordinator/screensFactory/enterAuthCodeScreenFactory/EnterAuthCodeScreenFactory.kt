package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory

import ru.terrakok.cicerone.android.support.SupportAppScreen

interface EnterAuthCodeScreenFactory {

    fun createEnterAuthCodeViewController(): SupportAppScreen

}