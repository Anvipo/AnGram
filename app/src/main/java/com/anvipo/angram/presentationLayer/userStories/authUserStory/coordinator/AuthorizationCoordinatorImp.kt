package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator

import android.content.Context
import com.anvipo.angram.applicationLayer.types.SystemMessageSendChannel
import com.anvipo.angram.applicationLayer.types.UpdateAuthorizationStateIReadOnlyStack
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.coreLayer.assertionFailure
import com.anvipo.angram.coreLayer.debugLog
import com.anvipo.angram.global.createTGSystemMessage
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinator
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen

class AuthorizationCoordinatorImp(
    private val context: Context,
    private val router: Router,
    private val screensFactory: AuthorizationScreensFactory,
    private val tdLibGateway: TDLibGateway,
    private val tdUpdateAuthorizationStateStack: UpdateAuthorizationStateIReadOnlyStack,
    private val systemMessageSendChannel: SystemMessageSendChannel
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
        val enterPhoneNumberScreen = createAndSetupEnterPhoneNumberScreen()

        router.newRootScreen(enterPhoneNumberScreen)
    }

    private fun showEnterAuthCodeScreen() {
        val (
            enterAuthCodeScreen,
            enteredCorrectAuthCodeReceiveChannel,
            backButtonPressedInAuthCodeScreenReceiveChannel
        ) = screensFactory.enterAuthCodeScreenFactory.createEnterAuthCodeViewController(tdLibGateway)

        handleEnteredCorrectAuthCode(enteredCorrectAuthCodeReceiveChannel)

        router.navigateTo(enterAuthCodeScreen)
    }

    private fun exit() {
        GlobalScope.launch(context = Dispatchers.Main) {
            router.exit()
        }
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

    private fun handleEnteredCorrectPhoneNumber(enteredCorrectPhoneNumberReceiveChannel: ReceiveChannel<String>) {
        val tag = "${this::class.java.simpleName} handleEnteredCorrectPhoneNumber"

        val onEnteredCorrectPhoneNumber: (String) -> Unit = { correctPhoneNumber ->
            val text = "$tag: onEnteredCorrectPhoneNumber: $correctPhoneNumber"

            systemMessageSendChannel.offer(createTGSystemMessage(text))

            showNeededScreen()
        }

        GlobalScope.launch {
            val receivedEnteredCorrectPhoneNumber = enteredCorrectPhoneNumberReceiveChannel.receive()

            onEnteredCorrectPhoneNumber(receivedEnteredCorrectPhoneNumber)
        }
    }

    private fun handleEnteredCorrectAuthCode(enteredCorrectAuthCodeReceiveChannel: ReceiveChannel<String>) {
        val tag = "${this::class.java.simpleName} handleEnteredCorrectAuthCode"

        val onEnteredCorrectAuthCode: (String) -> Unit = { correctAuthCode ->
            val text = "$tag: onEnteredCorrectAuthCode: $correctAuthCode"

            systemMessageSendChannel.offer(createTGSystemMessage(text))

            showNeededScreen()
        }

        GlobalScope.launch {
            val receivedEnteredCorrectAuthCode = enteredCorrectAuthCodeReceiveChannel.receive()

            onEnteredCorrectAuthCode(receivedEnteredCorrectAuthCode)
        }
    }

    private fun handleBackButtonPressedInPhoneNumberScreen(
        backButtonPressedInPhoneNumberScreenReceiveChannel: ReceiveChannel<Unit>
    ) {
        val tag = "${this::class.java.simpleName} handleBackButtonPressedInPhoneNumberScreen"

        val onBackButtonPressed: (Unit) -> Unit = { _ ->
            val text = "$tag: back button pressed in enter phone number screen"

            systemMessageSendChannel.offer(createTGSystemMessage(text))

            exit()
        }

        GlobalScope.launch {
            val receivedBackButtonPress = backButtonPressedInPhoneNumberScreenReceiveChannel.receive()

            onBackButtonPressed(receivedBackButtonPress)
        }
    }

    private fun createAndSetupEnterPhoneNumberScreen(): SupportAppScreen {
        val (
            enterPhoneNumberScreen,
            enteredCorrectPhoneNumberReceiveChannel,
            backButtonPressedInPhoneNumberScreenReceiveChannel
        ) = screensFactory.enterPhoneNumberScreenFactory.createEnterPhoneNumberViewController(tdLibGateway)

        handleBackButtonPressedInPhoneNumberScreen(backButtonPressedInPhoneNumberScreenReceiveChannel)

        handleEnteredCorrectPhoneNumber(enteredCorrectPhoneNumberReceiveChannel)

        return enterPhoneNumberScreen
    }

}