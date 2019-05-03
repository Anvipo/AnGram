package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory

import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.authorizationOptions.view.AuthorizationOptionsFragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.authorizationOptions.view.AuthorizationOptionsView
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.signIn.view.SignInFragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.signIn.view.SignInView
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.signUp.view.SignUpFragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.signUp.view.SignUpView

class AuthorizationViewControllersFactoryImp : AuthorizationViewControllersFactory {

    override fun createSignInViewController(): SignInView {
        return SignInFragment.createNewInstance()
    }

    override fun createSignUpViewController(): SignUpView {
        return SignUpFragment.createNewInstance()
    }

    override fun createAuthorizationOptionsViewController(): AuthorizationOptionsView {
        return AuthorizationOptionsFragment.createNewInstance()
    }

}