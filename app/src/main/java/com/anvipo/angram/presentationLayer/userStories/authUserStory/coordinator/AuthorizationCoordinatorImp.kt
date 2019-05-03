package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator

import com.anvipo.angram.applicationLayer.navigation.router.Routable
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinator
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.AuthorizationViewControllersFactory

class AuthorizationCoordinatorImp(
    private val router: Routable,
    private val viewControllersFactory: AuthorizationViewControllersFactory
) : BaseCoordinator(), AuthorizationCoordinator {

    override var finishFlow: (() -> Unit)? = null

    override fun start() {
        showAuthorizationOptionsScreen()
    }


    /// PRIVATE


    private fun showAuthorizationOptionsScreen() {
        val authorizationOptionsViewController = viewControllersFactory.createAuthorizationOptionsViewController()

        authorizationOptionsViewController.onSignIn = {
            showSignInScreen()
        }
        authorizationOptionsViewController.onSignUp = {
            showSignUpScreen()
        }

        router.setRootViewController(viewController = authorizationOptionsViewController)
    }

    private fun showSignUpScreen() {
        val signUpViewController = viewControllersFactory.createSignUpViewController()

        signUpViewController.onFinishFlow = onFinishFlow
        signUpViewController.onBackPressed = onBackPressed

        router.push(signUpViewController)
    }

    private fun showSignInScreen() {
        val signInViewController = viewControllersFactory.createSignInViewController()

        signInViewController.onFinishFlow = onFinishFlow
        signInViewController.onBackPressed = onBackPressed

        router.push(signInViewController)
    }

    private val onBackPressed: () -> Unit = {
        router.popViewController()
    }

    private val onFinishFlow: () -> Unit = {
        finishFlow?.invoke()
    }

}