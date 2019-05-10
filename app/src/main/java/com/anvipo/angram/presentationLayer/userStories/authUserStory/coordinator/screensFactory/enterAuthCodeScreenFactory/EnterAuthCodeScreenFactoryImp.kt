package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory

import androidx.fragment.app.Fragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view.EnterAuthCodeFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object EnterAuthCodeScreenFactoryImp : EnterAuthCodeScreenFactory {

    data class EnterAuthCodeScreen(
        private val shouldShowBackButton: Boolean,
        private val expectedCodeLength: Int,
        private val enteredPhoneNumber: String,
        private val registrationRequired: Boolean,
        private val termsOfServiceText: String
    ) : SupportAppScreen() {
        override fun getFragment(): Fragment =
            EnterAuthCodeFragment.createNewInstance(
                shouldShowBackButton = shouldShowBackButton,
                expectedCodeLength = expectedCodeLength,
                enteredPhoneNumber = enteredPhoneNumber,
                registrationRequired = registrationRequired,
                termsOfServiceText = termsOfServiceText
            ) as Fragment
    }

    override fun createEnterAuthCodeViewController(
        shouldShowBackButton: Boolean,
        expectedCodeLength: Int,
        enteredPhoneNumber: String,
        registrationRequired: Boolean,
        termsOfServiceText: String
    ): SupportAppScreen = EnterAuthCodeScreen(
        shouldShowBackButton = shouldShowBackButton,
        expectedCodeLength = expectedCodeLength,
        enteredPhoneNumber = enteredPhoneNumber,
        registrationRequired = registrationRequired,
        termsOfServiceText = termsOfServiceText
    )

}