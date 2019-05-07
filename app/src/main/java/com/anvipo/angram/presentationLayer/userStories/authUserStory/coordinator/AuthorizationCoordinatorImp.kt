package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator

import android.content.Context
import com.anvipo.angram.applicationLayer.types.SystemMessageSendChannel
import com.anvipo.angram.applicationLayer.types.UpdateAuthorizationStateIReadOnlyStack
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.coreLayer.assertionFailure
import com.anvipo.angram.coreLayer.debugLog
import com.anvipo.angram.coreLayer.message.SystemMessage
import com.anvipo.angram.global.createTGSystemMessage
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinator
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import ru.terrakok.cicerone.Router

class AuthorizationCoordinatorImp(
    private val context: Context,
    private val router: Router,
    private val screensFactory: AuthorizationScreensFactory,
    private val tdLibGateway: TDLibGateway,
    private val tdUpdateAuthorizationStateStack: UpdateAuthorizationStateIReadOnlyStack,
    private val systemMessageSendChannel: SystemMessageSendChannel
) : BaseCoordinator(), AuthorizationCoordinator {

    override fun coldStart() {
        showNeededScreen()
    }

    override fun onEnterCorrectPhoneNumber(phoneNumber: String) {
        val tag = "${this::class.java.simpleName} onEnterCorrectPhoneNumber"

        val text = "$tag: onEnteredCorrectPhoneNumber: $phoneNumber"

        systemMessageSendChannel.offer(createLogMessage(text))

        showNeededScreen()
    }

    override fun onPressedBackButtonInEnterPhoneNumberScreen() {
        val tag = "${this::class.java.simpleName} onPressedBackButtonInEnterPhoneNumberScreen"

        val text = "$tag: back button pressed in enter phone number screen"

        systemMessageSendChannel.offer(createLogMessage(text))

        router.exit()
    }

    override var finishFlow: (() -> Unit)? = null


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
                    showEnterAuthCodeScreen()
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
                debugLog(currentAuthorizationState.toString())

                // TODO: handle this case
                assertionFailure()
            }
        }
    }


    private fun showEnterPhoneNumberScreen() {
        val enterPhoneNumberScreen =
            screensFactory.enterPhoneNumberScreenFactory.createEnterPhoneNumberViewController(tdLibGateway)

        router.newRootScreen(enterPhoneNumberScreen)
    }

    private fun showEnterAuthCodeScreen() {
        val enterAuthCodeScreen =
            screensFactory.enterAuthCodeScreenFactory.createEnterAuthCodeViewController()

        TODO()

        val tag = "${this::class.java.simpleName} handleEnteredCorrectAuthCode"

//        val text = "$tag: onEnteredCorrectAuthCode: $correctAuthCode"

//        systemMessageSendChannel.offer(createTGSystemMessage(text))

        showNeededScreen()

        router.navigateTo(enterAuthCodeScreen)
    }

    private val onFinishFlow: () -> Unit = {
        finishFlow?.invoke()
    }


    private fun onSuccessSetTDLibParametersResult(tag: String): (TdApi.Ok) -> Unit = { _ ->
        val text = "$tag: TDLib successfully set parameters"
        systemMessageSendChannel.offer(createTGSystemMessage(text))

        showNeededScreen()
    }

    private fun onFailureSetTDLibParametersResult(tag: String): (Throwable) -> Unit = { error ->
        val text = "$tag: setTDLibParameters.onFailure: ${error.localizedMessage}"
        systemMessageSendChannel.offer(createTGSystemMessage(text))

        showNeededScreen()
    }


    private fun onSuccessCheckDatabaseEncryptionKeyResult(tag: String): (TdApi.Ok) -> Unit = { _ ->
        val text = "$tag: TDLib successfully checked database encryption key"
        systemMessageSendChannel.offer(createTGSystemMessage(text))

        showNeededScreen()
    }

    private fun onFailureCheckDatabaseEncryptionKeyResult(tag: String): (Throwable) -> Unit = { error ->
        val text = "$tag: setTDLibParameters.onFailure: ${error.localizedMessage}"
        systemMessageSendChannel.offer(createTGSystemMessage(text))

        showNeededScreen()
    }

    private fun createLogMessage(text: String): SystemMessage = SystemMessage(
        text,
        shouldBeShownToUser = false,
        shouldBeShownInLogs = true
    )

}