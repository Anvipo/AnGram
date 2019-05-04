package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator

import android.content.Context
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.coreLayer.assertionFailure
import com.anvipo.angram.coreLayer.collections.IReadOnlyStack
import com.anvipo.angram.coreLayer.message.SystemMessageNotifier
import com.anvipo.angram.global.createTGSystemMessage
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinator
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.AuthorizationViewControllersFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen

class AuthorizationCoordinatorImp(
    private val context: Context,
    private val router: Router,
    private val viewControllersFactory: AuthorizationViewControllersFactory,
    private val tdLibGateway: TDLibGateway,
    private val tdUpdateAuthorizationStateStack: IReadOnlyStack<TdApi.UpdateAuthorizationState>,
    private val systemMessageNotifier: SystemMessageNotifier
) : BaseCoordinator(), AuthorizationCoordinator {

    override var finishFlow: (() -> Unit)? = null

    override fun coldStart() {
        showNeededScreen()
    }


    /// PRIVATE


    private fun showNeededScreen() {
        val tag = "${this::class.java.simpleName} showNeededScreen"

        // TODO: get last element
        val lastAuthorizationState = tdUpdateAuthorizationStateStack.peek()

        if (lastAuthorizationState == null) {
            // TODO: handle this case
            assertionFailure()
            return
        }

        when (val currentAuthorizationState = lastAuthorizationState.authorizationState) {
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
        val enterPhoneNumberScreen = createAndSetupEnterPhoneNumberScreen()

        router.newRootScreen(enterPhoneNumberScreen)
    }

    private fun createAndSetupEnterPhoneNumberScreen(): SupportAppScreen {
        val tag = "${this::class.java.simpleName} showEnterPhoneNumberScreen"

        val (enterPhoneNumberView, enterPhoneNumberScreen) =
            viewControllersFactory
                .createEnterPhoneNumberViewController(tdLibGateway)

        enterPhoneNumberView.onEnteredCorrectPhoneNumber = { correctPhoneNumber ->
            val message = "$tag: onEnteredCorrectPhoneNumber: $correctPhoneNumber"

            systemMessageNotifier.send(text = message)

            showNeededScreen()
        }

        return enterPhoneNumberScreen
    }

    private fun showEnterAuthCodeScreen(currentAuthorizationState: TdApi.AuthorizationStateWaitCode) {
        val (enterAuthCodeView, enterAuthCodeScreen) = viewControllersFactory
            .createEnterAuthCodeViewController(tdLibGateway)

        enterAuthCodeView.onBackPressed = onBackPressed

        router.navigateTo(enterAuthCodeScreen)
    }

    private val onBackPressed: () -> Unit = {
        router.backTo(null)
    }

    private val onFinishFlow: () -> Unit = {
        finishFlow?.invoke()
    }


    private fun onSuccessSetTDLibParametersResult(tag: String): (TdApi.Ok) -> Unit = { _ ->
        val text = "$tag: TDLib successfully set parameters"
        systemMessageNotifier.send(createTGSystemMessage(text))

        showNeededScreen()
    }

    private fun onFailureSetTDLibParametersResult(tag: String): (Throwable) -> Unit = { error ->
        val text = "$tag: setTDLibParameters.onFailure: ${error.localizedMessage}"
        systemMessageNotifier.send(createTGSystemMessage(text))

        showNeededScreen()
    }


    private fun onSuccessCheckDatabaseEncryptionKeyResult(tag: String): (TdApi.Ok) -> Unit = { _ ->
        val text = "$tag: TDLib successfully checked database encryption key"
        systemMessageNotifier.send(createTGSystemMessage(text))

        showNeededScreen()
    }

    private fun onFailureCheckDatabaseEncryptionKeyResult(tag: String): (Throwable) -> Unit = { error ->
        val text = "$tag: setTDLibParameters.onFailure: ${error.localizedMessage}"
        systemMessageNotifier.send(createTGSystemMessage(text))

        showNeededScreen()
    }

}