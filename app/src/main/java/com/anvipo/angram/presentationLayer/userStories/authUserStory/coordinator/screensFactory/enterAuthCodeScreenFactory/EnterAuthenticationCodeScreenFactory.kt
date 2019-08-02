package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory

import ru.terrakok.cicerone.android.support.SupportAppScreen

interface EnterAuthenticationCodeScreenFactory {

    fun createEnterAuthCodeScreen(
        expectedCodeLength: Int,
        enteredPhoneNumber: String,
        registrationRequired: Boolean,
        termsOfServiceText: String,
        shouldShowBackButton: Boolean
    ): SupportAppScreen

}