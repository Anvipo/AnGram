package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator

import com.anvipo.angram.applicationLayer.navigation.router.Routable
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinator
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.AuthorizationScreensFactory

class AuthorizationCoordinatorImp(
    private val router: Routable,
    private val screensFactory: AuthorizationScreensFactory
) : BaseCoordinator(), AuthorizationCoordinator {

    override var finishFlow: (() -> Unit)? = null

    override fun start() {
        showAuthorizationOptionsModule()
    }


    /// PRIVATE


    private fun showAuthorizationOptionsModule() {
        val authorizationOptionsModule = screensFactory.createAuthorizationOptionsScreen()

        authorizationOptionsModule.onBackPressed = {
            TODO()
        }
        authorizationOptionsModule.onSignIn = {
            showSignInModule()
        }
        authorizationOptionsModule.onSignUp = {
            showSignUpModule()
        }

        router.set(rootScreen = authorizationOptionsModule)
    }

    private fun showSignUpModule() {
        val signUpModule = screensFactory.createSignUpScreen()

        signUpModule.onBackPressed = {
            TODO()
        }

        router.push(signUpModule)
    }

    private fun showSignInModule() {
        val signInModule = screensFactory.createSignInScreen()

        signInModule.onBackPressed = {
            TODO()
        }

        router.push(signInModule)
    }

}