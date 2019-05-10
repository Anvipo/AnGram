package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory

import ru.terrakok.cicerone.android.support.SupportAppScreen

interface EnterAuthCodeScreenFactory {

    fun createEnterAuthCodeViewController(
        shouldShowBackButton: Boolean,
        expectedCodeLength: Int,
        enteredPhoneNumber: String,
        registrationRequired: Boolean,
        termsOfServiceText: String
    ): SupportAppScreen

}