package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory

import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.authorizationOptions.view.AuthorizationOptionsView
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.signIn.view.SignInView
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.signUp.view.SignUpView

interface AuthorizationViewControllersFactory {

    fun createAuthorizationOptionsViewController(): AuthorizationOptionsView

    fun createSignInViewController(): SignInView

    fun createSignUpViewController(): SignUpView

}