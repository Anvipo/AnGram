package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory

import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.authorizationOptions.view.AuthorizationOptionsView
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.signIn.view.SignInView
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.signUp.view.SignUpView

interface AuthorizationScreensFactory {

    fun createAuthorizationOptionsScreen(): AuthorizationOptionsView

    fun createSignInScreen(): SignInView

    fun createSignUpScreen(): SignUpView

}