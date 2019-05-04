package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator

import com.anvipo.angram.applicationLayer.launchSystem.AppActivity
import com.anvipo.angram.applicationLayer.navigation.router.Routable
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.global.assertionFailure
import com.anvipo.angram.global.debugLog
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinator
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.AuthorizationViewControllersFactory
import kotlinx.coroutines.Dispatchers
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
        showNeededScreen()
    }


    /// PRIVATE


    private fun showNeededScreen() {
        val tag = "${this::class.java.simpleName} showNeededScreen"

        when (val currentAuthorizationState = AppActivity.lastAuthorizationState.authorizationState) {
            is TdApi.AuthorizationStateWaitPhoneNumber -> {
                GlobalScope.launch(context = Dispatchers.Main) {
                    showEnterPhoneNumberScreen()
                }
            }
            is TdApi.AuthorizationStateWaitCode -> {
                GlobalScope.launch(context = Dispatchers.Main) {
                    showEnterAuthCodeScreen(currentAuthorizationState)
                }
            }
            is TdApi.AuthorizationStateWaitTdlibParameters -> {
                val context = router.rootController.get()?.thisContext

                @Suppress("FoldInitializerAndIfToElvis")
                if (context == null) {
                    return
                }

                GlobalScope.launch {
                    val setTdLibParametersResult = tdLibGateway.setTdLibParametersCatching(context)

                    setTdLibParametersResult
                        .onSuccess(onSuccessSetTDLibParametersResult(tag))
                        .onFailure(onFailureSetTDLibParametersResult(tag))
                }
            }
            is TdApi.AuthorizationStateWaitEncryptionKey -> {
                GlobalScope.launch {
                    val checkDatabaseEncryptionKeyResult = tdLibGateway.checkDatabaseEncryptionKeyCatching()

                    checkDatabaseEncryptionKeyResult
                        .onSuccess(onSuccessCheckDatabaseEncryptionKeyResult(tag))
                        .onFailure(onFailureCheckDatabaseEncryptionKeyResult(tag))
                }
            }
            else -> {
                // TODO: handle this case
                assertionFailure()
            }
        }
    }


    private fun showEnterPhoneNumberScreen() {
        val tag = "${this::class.java.simpleName} showEnterPhoneNumberScreen"

        val enterPhoneNumberScreen = viewControllersFactory.createEnterPhoneNumberViewController(tdLibGateway)

        enterPhoneNumberScreen.onEnteredCorrectPhoneNumber = { correctPhoneNumber ->
            val message = "$tag: onEnteredCorrectPhoneNumber: $correctPhoneNumber"

            debugLog(message)

            showNeededScreen()
        }

        router.setRootViewController(enterPhoneNumberScreen)
    }

    private fun showEnterAuthCodeScreen(currentAuthorizationState: TdApi.AuthorizationStateWaitCode) {
        val enterPhoneNumberScreen = viewControllersFactory.createEnterAuthCodeViewController(tdLibGateway)

        enterPhoneNumberScreen.onBackPressed = onBackPressed

        router.push(enterPhoneNumberScreen)
    }

    private val onBackPressed: () -> Unit = {
        router.popViewController()
    }

    private val onFinishFlow: () -> Unit = {
        finishFlow?.invoke()
    }


    private fun onSuccessSetTDLibParametersResult(tag: String): (TdApi.Ok) -> Unit = { _ ->
        val message = "$tag: TDLib successfully set parameters"
        debugLog(message)

        showNeededScreen()
    }

    private fun onFailureSetTDLibParametersResult(tag: String): (Throwable) -> Unit = { error ->
        val message = "$tag: setTDLibParameters.onFailure: ${error.localizedMessage}"
        debugLog(message)

        showNeededScreen()
    }


    private fun onSuccessCheckDatabaseEncryptionKeyResult(tag: String): (TdApi.Ok) -> Unit = { _ ->
        val message = "$tag: TDLib successfully checked database encryption key"
        debugLog(message)

        showNeededScreen()
    }

    private fun onFailureCheckDatabaseEncryptionKeyResult(tag: String): (Throwable) -> Unit = { error ->
        val message = "$tag: setTDLibParameters.onFailure: ${error.localizedMessage}"
        debugLog(message)

        showNeededScreen()
    }

}