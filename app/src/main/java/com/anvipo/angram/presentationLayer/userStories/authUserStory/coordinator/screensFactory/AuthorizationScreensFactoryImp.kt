package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory

import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.authorizationOptions.view.AuthorizationOptionsFragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.authorizationOptions.view.AuthorizationOptionsView
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.signIn.view.SignInFragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.signIn.view.SignInView
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.signUp.view.SignUpView

class AuthorizationScreensFactoryImp : AuthorizationScreensFactory {

    override fun createSignInScreen(): SignInView {
        return SignInFragment.createNewInstance()
    }

    override fun createSignUpScreen(): SignUpView {
        TODO("not implemented")
    }

    override fun createAuthorizationOptionsScreen(): AuthorizationOptionsView {
        return AuthorizationOptionsFragment.createNewInstance()
    }

}