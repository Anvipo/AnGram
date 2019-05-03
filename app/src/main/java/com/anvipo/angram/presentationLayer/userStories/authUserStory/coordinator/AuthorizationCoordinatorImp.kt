package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator

import com.anvipo.angram.applicationLayer.navigation.router.Routable
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.global.assertionFailure
import com.anvipo.angram.global.debugLog
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinator
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.AuthorizationViewControllersFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi

class AuthorizationCoordinatorImp(
    private val router: Routable,
    private val viewControllersFactory: AuthorizationViewControllersFactory,
    private val tdLibGateway: TDLibGateway
) : BaseCoordinator(), AuthorizationCoordinator {

    override var finishFlow: (() -> Unit)? = null

    override fun start() {
        val tag = "${this::class.java.simpleName} start"

        GlobalScope.launch {
            val context = router.rootController.get()?.thisContext

            @Suppress("FoldInitializerAndIfToElvis")
            if (context == null) {
                return@launch
            }

            val setTdLibParametersResult = tdLibGateway.setTdLibParametersCatching(
                context = context
            )

            setTdLibParametersResult.onSuccess { setTdLibParametersTdApiObject ->
                when (setTdLibParametersTdApiObject) {
                    is TdApi.Ok -> {
                        val message = "$tag: TdApi.Ok"

                        debugLog(message)
                    }
                    else -> {
                        assertionFailure()
                    }
                }
            }

            setTdLibParametersResult.onFailure {
                assertionFailure()
            }
        }
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