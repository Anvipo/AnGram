package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator

import android.content.Context
import com.anvipo.angram.applicationLayer.types.SystemMessageSendChannel
import com.anvipo.angram.applicationLayer.types.UpdateAuthorizationStateIReadOnlyStack
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.coreLayer.CoreHelpers.assertionFailure
import com.anvipo.angram.coreLayer.CoreHelpers.debugLog
import com.anvipo.angram.coreLayer.message.SystemMessage
import com.anvipo.angram.global.CoreHelpers.createTGSystemMessage
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinator
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactory
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.types.CorrectPhoneNumberType
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import ru.terrakok.cicerone.Router
import kotlin.coroutines.CoroutineContext

class AuthorizationCoordinatorImp(
    private val context: Context,
    private val router: Router,
    private val screensFactory: AuthorizationScreensFactory,
    private val tdLibGateway: TDLibGateway,
    private val tdUpdateAuthorizationStateStack: UpdateAuthorizationStateIReadOnlyStack,
    private val systemMessageSendChannel: SystemMessageSendChannel
) : BaseCoordinator(), AuthorizationCoordinatorInput, AuthorizationCoordinatorOutput {

    override fun coldStart() {
        showNeededScreen()
    }

    override fun onEnterCorrectPhoneNumber(phoneNumber: CorrectPhoneNumberType) {
        val tag = "${this::class.java.simpleName} onEnterCorrectPhoneNumber"

        val text = "$tag: entered correct phone number: $phoneNumber"

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

    override fun cancelAllJobs() {
        setTdLibParametersCatchingJob?.cancel()
        showEnterPhoneNumberScreenJob?.cancel()
        checkDatabaseEncryptionKeyCatchingJob?.cancel()
    }

    override val coroutineContext: CoroutineContext = Dispatchers.IO


    /// PRIVATE


    private fun showNeededScreen() {
        val tag = "${this::class.java.simpleName} showNeededScreen"

        val lastAuthorizationState = tdUpdateAuthorizationStateStack.peek()

        if (lastAuthorizationState == null) {
            // TODO: handle this case
            assertionFailure()
            return
        }

        when (val currentAuthorizationState = lastAuthorizationState.authorizationState) {
            is TdApi.AuthorizationStateWaitPhoneNumber -> {
                val showEnterPhoneNumberScreenCoroutineExceptionHandler =
                    CoroutineExceptionHandler { _, throwable ->
                        val text = throwable.localizedMessage

                        systemMessageSendChannel.offer(createTGSystemMessage(text))
                    }

                showEnterPhoneNumberScreenJob = launch(
                    context = coroutineContext + showEnterPhoneNumberScreenCoroutineExceptionHandler
                ) {
                    showEnterPhoneNumberScreen()
                }
            }
            is TdApi.AuthorizationStateWaitCode -> {
                val showEnterAuthCodeScreenCoroutineExceptionHandler =
                    CoroutineExceptionHandler { _, throwable ->
                        val text = throwable.localizedMessage

                        systemMessageSendChannel.offer(createTGSystemMessage(text))
                    }

                showEnterAuthCodeScreenJob = launch(
                    context = coroutineContext + showEnterAuthCodeScreenCoroutineExceptionHandler
                ) {
                    showEnterAuthCodeScreen()
                }
            }
            is TdApi.AuthorizationStateWaitTdlibParameters -> {
                val setTdLibParametersCatchingCoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
                    val text = throwable.localizedMessage

                    systemMessageSendChannel.offer(createTGSystemMessage(text))
                }

                setTdLibParametersCatchingJob = launch(
                    context = coroutineContext + setTdLibParametersCatchingCoroutineExceptionHandler
                ) {
                    val setTdLibParametersResult = tdLibGateway.setTdLibParametersCatching(context)

                    setTdLibParametersResult
                        .onSuccess(onSuccessSetTDLibParametersResult(tag))
                        .onFailure(onFailureSetTDLibParametersResult(tag))
                }
            }
            is TdApi.AuthorizationStateWaitEncryptionKey -> {
                val checkDatabaseEncryptionKeyCatchingCoroutineExceptionHandler =
                    CoroutineExceptionHandler { _, throwable ->
                        val text = throwable.localizedMessage

                        systemMessageSendChannel.offer(createTGSystemMessage(text))
                    }

                checkDatabaseEncryptionKeyCatchingJob = launch(
                    context = coroutineContext + checkDatabaseEncryptionKeyCatchingCoroutineExceptionHandler
                ) {
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
            screensFactory
                .enterPhoneNumberScreenFactory
                .createEnterPhoneNumberViewController(tdLibGateway)

        router.newRootScreen(enterPhoneNumberScreen)
    }

    private fun showEnterAuthCodeScreen() {
        val enterAuthCodeScreen =
            screensFactory
                .enterAuthCodeScreenFactory
                .createEnterAuthCodeViewController()

        TODO()

        val tag = "${this::class.java.simpleName} handleEnteredCorrectAuthCode"

//        val text = "$tag: onEnteredCorrectAuthCode: $correctAuthCode"

//        systemMessageSendChannel.offer(createTGSystemMessage(text))

        router.navigateTo(enterAuthCodeScreen)
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

    private var setTdLibParametersCatchingJob: Job? = null
    private var showEnterPhoneNumberScreenJob: Job? = null
    private var showEnterAuthCodeScreenJob: Job? = null
    private var checkDatabaseEncryptionKeyCatchingJob: Job? = null

}