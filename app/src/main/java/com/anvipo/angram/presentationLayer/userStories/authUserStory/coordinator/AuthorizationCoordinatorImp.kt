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

        authorizationOptionsModule.onSignIn = {
            showSignInModule()
        }
        authorizationOptionsModule.onSignUp = {
            showSignUpModule()
        }

        router.setRootViewController(viewController = authorizationOptionsModule)
    }

    private fun showSignUpModule() {
        val signUpModule = screensFactory.createSignUpScreen()

        signUpModule.onBackPressed = onBackPressed

        router.push(signUpModule)
    }

    private fun showSignInModule() {
        val signInModule = screensFactory.createSignInScreen()

        signInModule.onFinishFlow = onFinishFlow

        signInModule.onBackPressed = onBackPressed

        router.push(signInModule)
    }

    private val onBackPressed: () -> Unit = {
        router.popViewController()
    }

    private val onFinishFlow: () -> Unit = {
        finishFlow?.invoke()
    }

}