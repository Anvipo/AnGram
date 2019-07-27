package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator

import com.anvipo.angram.applicationLayer.types.SystemMessageSendChannel
import com.anvipo.angram.coreLayer.CoreHelpers.assertionFailure
import com.anvipo.angram.coreLayer.CoreHelpers.debugLog
import com.anvipo.angram.coreLayer.message.SystemMessage
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.global.GlobalHelpers.createTGSystemMessage
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinatorImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinator
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationCodeRouteEventHandler
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationPasswordRouteEventHandler
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactory
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterPhoneNumberScreenFactory.EnterPhoneNumberScreenFactoryImp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import ru.terrakok.cicerone.Router
import kotlin.coroutines.CoroutineContext

class AuthorizationCoordinatorImp(
    private val router: Router,
    private val screensFactory: AuthorizationScreensFactory,
    private val tdLibGateway: AuthorizationTDLibGateway,
    private val systemMessageSendChannel: SystemMessageSendChannel
) : BaseCoordinatorImp(),
    AuthorizationCoordinator,
    AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler,
    AuthorizationCoordinatorEnterAuthenticationCodeRouteEventHandler,
    AuthorizationCoordinatorEnterAuthenticationPasswordRouteEventHandler {

    override fun coldStart() {
        checkAuthState()
    }

    override fun startAuthorizationFlowWithEnterAuthorizationCodeAsRootScreen() {
        val getAuthorizationStateRequestCatchingCoroutineExceptionHandler =
            CoroutineExceptionHandler { _, error ->
                val errorText = error.localizedMessage

                systemMessageSendChannel.offer(createTGSystemMessage(errorText))
            }

        getAuthorizationStateRequestCatchingJob = launch(
            context = coroutineContext + getAuthorizationStateRequestCatchingCoroutineExceptionHandler
        ) {
            val authorizationStateResult = tdLibGateway.getAuthorizationStateRequestCatching()

            authorizationStateResult
                .onSuccess {
                    if (it is TdApi.AuthorizationStateWaitCode) {
                        onAuthStateWaitsCode(
                            it,
                            withEnterAuthCodeAsRootScreen = true
                        )
                    } else {
                        onSuccessGetAuthStateResult(it)
                    }
                }
                .onFailure(::onFailureGetAuthStateResult)
        }
    }

    override fun startAuthFlowWithEnterPasswordAsRootScreen() {
        val getAuthorizationStateRequestCatchingCoroutineExceptionHandler =
            CoroutineExceptionHandler { _, error ->
                val errorText = error.localizedMessage

                systemMessageSendChannel.offer(createTGSystemMessage(errorText))
            }

        getAuthorizationStateRequestCatchingJob = launch(
            context = coroutineContext + getAuthorizationStateRequestCatchingCoroutineExceptionHandler
        ) {
            val authorizationStateResult = tdLibGateway.getAuthorizationStateRequestCatching()

            authorizationStateResult
                .onSuccess {
                    if (it is TdApi.AuthorizationStateWaitPassword) {
                        onAuthorizationStateWaitPassword(
                            it,
                            withEnterAuthenticationPasswordAsRootScreen = true
                        )
                    } else {
                        onSuccessGetAuthStateResult(it)
                    }
                }
                .onFailure(::onFailureGetAuthStateResult)
        }

    }

    override fun onEnterCorrectPhoneNumber() {
        val tag = "${this::class.java.simpleName} onEnterCorrectPhoneNumber"

        val text = "$tag: entered correct phone number"

        systemMessageSendChannel.offer(createLogMessage(text))

        checkAuthState()
    }

    override fun onPressedBackButtonInEnterPhoneNumberScreen() {
        val tag = "${this::class.java.simpleName} onPressedBackButtonInEnterPhoneNumberScreen"

        val text = "$tag: back button pressed in enter phone number screen"

        debugLog(text)

        router.exit()
    }

    override fun onPressedBackButtonInEnterAuthenticationCodeScreen() {
        val tag = "${this::class.java.simpleName} onPressedBackButtonInEnterAuthenticationCodeScreen"

        val text = "$tag: back button pressed in enter auth code screen"

        systemMessageSendChannel.offer(createLogMessage(text))

        router.exit()
    }

    override fun onEnterCorrectAuthenticationCode() {
        val tag = "${this::class.java.simpleName} onEnterCorrectAuthenticationCode"

        val text = "$tag: entered correct auth code"

        systemMessageSendChannel.offer(createLogMessage(text))

        checkAuthState()
    }

    override fun onPressedBackButtonInEnterAuthenticationPasswordScreen() {
        val tag = "${this::class.java.simpleName} onPressedBackButtonInEnterAuthenticationPasswordScreen"

        val text = "$tag: back button pressed in enter password screen"

        systemMessageSendChannel.offer(createLogMessage(text))

        router.backTo(EnterPhoneNumberScreenFactoryImp.EnterPhoneNumberScreen)
    }

    override fun onEnterCorrectAuthenticationPassword() {
        val tag = "${this::class.java.simpleName} onEnterCorrectAuthenticationPassword"

        val text = "$tag: entered correct password"

        systemMessageSendChannel.offer(createLogMessage(text))

        checkAuthState()
    }


    override var finishFlow: (() -> Unit)? = null

    override fun cancelAllJobs() {
        getAuthorizationStateRequestCatchingJob?.cancel()
        setTdLibParametersCatchingJob?.cancel()
        checkDatabaseEncryptionKeyCatchingJob?.cancel()
        showEnterPhoneNumberScreenJob?.cancel()
        showEnterAuthCodeScreenJob?.cancel()
        showEnterPasswordScreenJob?.cancel()
    }

    override val coroutineContext: CoroutineContext = Dispatchers.IO


    /// PRIVATE

    private fun checkAuthState() {
        val getAuthorizationStateRequestCatchingCoroutineExceptionHandler =
            CoroutineExceptionHandler { _, error ->
                val errorText = error.localizedMessage

                systemMessageSendChannel.offer(createTGSystemMessage(errorText))
            }

        getAuthorizationStateRequestCatchingJob = launch(
            context = coroutineContext + getAuthorizationStateRequestCatchingCoroutineExceptionHandler
        ) {
            val authorizationStateResult = tdLibGateway.getAuthorizationStateRequestCatching()

            authorizationStateResult
                .onSuccess(::onSuccessGetAuthStateResult)
                .onFailure(::onFailureGetAuthStateResult)
        }
    }

    private fun onSuccessGetAuthStateResult(authState: TdApi.AuthorizationState) {
        showNeededScreen(authState)
    }

    private fun onFailureGetAuthStateResult(error: Throwable) {
        val tag = "${this::class.java.simpleName} onFailureGetAuthStateResult"
        val text = "$tag: ${error.localizedMessage}"

        systemMessageSendChannel.offer(SystemMessage(text = text))

        checkAuthState()
    }


    private fun showNeededScreen(authState: TdApi.AuthorizationState) {
        when (authState) {
            is TdApi.AuthorizationStateWaitPhoneNumber -> onAuthStateWaitsPhoneNumber()
            is TdApi.AuthorizationStateWaitCode -> onAuthStateWaitsCode(authState)
            is TdApi.AuthorizationStateWaitPassword -> onAuthorizationStateWaitPassword(authState)
            is TdApi.AuthorizationStateReady -> onAuthorizationStateReady()
            else -> {
                val tag = "${this::class.java.simpleName} showNeededScreen"
                assertionFailure("$tag: Undefined authState: currentAuthorizationState.toString()")
            }
        }
    }


    private fun onAuthStateWaitsPhoneNumber() {
        val showEnterPhoneNumberScreenCoroutineExceptionHandler =
            CoroutineExceptionHandler { _, error ->
                val errorText = error.localizedMessage

                systemMessageSendChannel.offer(createTGSystemMessage(errorText))
            }

        showEnterPhoneNumberScreenJob = launch(
            context = Dispatchers.Main + showEnterPhoneNumberScreenCoroutineExceptionHandler
        ) {

            showEnterPhoneNumberScreen()
        }
    }

    private fun onAuthStateWaitsCode(
        currentAuthorizationState: TdApi.AuthorizationStateWaitCode,
        withEnterAuthCodeAsRootScreen: Boolean = false
    ) {
        val showEnterAuthCodeScreenCoroutineExceptionHandler =
            CoroutineExceptionHandler { _, error ->
                val errorText = error.localizedMessage

                systemMessageSendChannel.offer(createTGSystemMessage(errorText))
            }

        val (
            expectedCodeLength,
            enteredPhoneNumber,
            registrationRequired,
            termsOfServiceText
        ) = prepareParametersForEnterAuthCodeScreen(currentAuthorizationState)

        showEnterAuthCodeScreenJob = launch(
            context = Dispatchers.Main + showEnterAuthCodeScreenCoroutineExceptionHandler
        ) {
            showEnterAuthCodeScreen(
                withEnterAuthCodeAsRootScreen = withEnterAuthCodeAsRootScreen,
                expectedCodeLength = expectedCodeLength,
                enteredPhoneNumber = enteredPhoneNumber,
                registrationRequired = registrationRequired,
                termsOfServiceText = termsOfServiceText
            )
        }
    }

    private fun onAuthorizationStateWaitPassword(
        authState: TdApi.AuthorizationStateWaitPassword,
        withEnterAuthenticationPasswordAsRootScreen: Boolean = false
    ) {
        val showEnterPasswordScreenCoroutineExceptionHandler =
            CoroutineExceptionHandler { _, error ->
                val errorText = error.localizedMessage

                systemMessageSendChannel.offer(createTGSystemMessage(errorText))
            }

        showEnterPasswordScreenJob = launch(
            context = Dispatchers.Main + showEnterPasswordScreenCoroutineExceptionHandler
        ) {

            showEnterAuthenticationPasswordScreen(
                withEnterAuthenticationPasswordAsRootScreen = withEnterAuthenticationPasswordAsRootScreen
            )
        }
    }

    private fun showEnterAuthenticationPasswordScreen(withEnterAuthenticationPasswordAsRootScreen: Boolean) {
        if (withEnterAuthenticationPasswordAsRootScreen) {
            val enterPhoneNumberScreen =
                screensFactory
                    .enterPhoneNumberScreenFactory
                    .createEnterPhoneNumberViewController(tdLibGateway)

            val enterPasswordScreen =
                screensFactory
                    .enterPasswordScreenFactory
                    .createEnterPasswordViewController()

            router.newRootChain(enterPhoneNumberScreen, enterPasswordScreen)
        } else {
            val enterPasswordScreen =
                screensFactory
                    .enterPasswordScreenFactory
                    .createEnterPasswordViewController()

            router.navigateTo(enterPasswordScreen)
        }
    }

    private fun onAuthorizationStateReady() {
        val tag = "${this::class.java.simpleName} onAuthorizationStateReady"
        val text = "$tag: successful authorization"
        systemMessageSendChannel.offer(createTGSystemMessage(text))

        onFinishFlowWrapper()
    }


    private fun showEnterPhoneNumberScreen() {
        val enterPhoneNumberScreen =
            screensFactory
                .enterPhoneNumberScreenFactory
                .createEnterPhoneNumberViewController(tdLibGateway)

        router.newRootScreen(enterPhoneNumberScreen)
    }

    private fun showEnterAuthCodeScreen(
        withEnterAuthCodeAsRootScreen: Boolean = false,
        expectedCodeLength: Int = 5,
        enteredPhoneNumber: String = "",
        registrationRequired: Boolean,
        termsOfServiceText: String
    ) {
        if (withEnterAuthCodeAsRootScreen) {
            val enterAuthCodeScreen =
                screensFactory
                    .enterAuthenticationCodeScreenFactory
                    .createEnterAuthCodeViewController(
                        expectedCodeLength = expectedCodeLength,
                        enteredPhoneNumber = enteredPhoneNumber,
                        registrationRequired = registrationRequired,
                        termsOfServiceText = termsOfServiceText
                    )

            val enterPhoneNumberScreen =
                screensFactory
                    .enterPhoneNumberScreenFactory
                    .createEnterPhoneNumberViewController(tdLibGateway)

            router.newRootChain(enterPhoneNumberScreen, enterAuthCodeScreen)
        } else {
            val enterAuthCodeScreen =
                screensFactory
                    .enterAuthenticationCodeScreenFactory
                    .createEnterAuthCodeViewController(
                        expectedCodeLength = expectedCodeLength,
                        enteredPhoneNumber = enteredPhoneNumber,
                        registrationRequired = registrationRequired,
                        termsOfServiceText = termsOfServiceText
                    )

            router.navigateTo(enterAuthCodeScreen)
        }
    }

    private data class EnterAuthCodeScreenParameters(
        val expectedCodeLength: Int,
        val enteredPhoneNumber: String,
        val registrationRequired: Boolean,
        val termsOfServiceText: String
    )

    private fun prepareParametersForEnterAuthCodeScreen(
        currentAuthorizationState: TdApi.AuthorizationStateWaitCode
    ): EnterAuthCodeScreenParameters {
        val codeInfo = currentAuthorizationState.codeInfo

        val expectedCodeLength: Int = when (val codeInfoType = codeInfo.type) {
            is TdApi.AuthenticationCodeTypeSms -> codeInfoType.length
            is TdApi.AuthenticationCodeTypeCall -> codeInfoType.length
            is TdApi.AuthenticationCodeTypeTelegramMessage -> codeInfoType.length
            is TdApi.AuthenticationCodeTypeFlashCall -> {
                val pattern = codeInfoType.pattern
                debugLog("codeInfoType is TdApi.AuthenticationCodeTypeFlashCall: pattern = $pattern")
                0
            }
            else -> {
                assertionFailure("Undefined codeInfoType")
                0
            }
        }

        val enteredPhoneNumber = codeInfo.phoneNumber

        val termsOfServiceText: String
        val registrationRequired = !currentAuthorizationState.isRegistered

        if (registrationRequired) {
            val termsOfService = currentAuthorizationState.termsOfService

            termsOfServiceText = if (termsOfService != null) {
                val entities = termsOfService.text.entities
                if (entities.isNotEmpty()) {
                    debugLog("termsOfService.text.entities.isNotEmpty: $entities")
                }

                termsOfService.text.text
            } else {
                ""
            }
        } else {
            termsOfServiceText = ""
        }

        return EnterAuthCodeScreenParameters(
            expectedCodeLength = expectedCodeLength,
            enteredPhoneNumber = enteredPhoneNumber,
            registrationRequired = registrationRequired,
            termsOfServiceText = termsOfServiceText
        )
    }


    private fun createLogMessage(text: String): SystemMessage = SystemMessage(
        text,
        shouldBeShownToUser = false,
        shouldBeShownInLogs = true
    )

    private var getAuthorizationStateRequestCatchingJob: Job? = null
    private var setTdLibParametersCatchingJob: Job? = null
    private var checkDatabaseEncryptionKeyCatchingJob: Job? = null
    private var showEnterPhoneNumberScreenJob: Job? = null
    private var showEnterAuthCodeScreenJob: Job? = null
    private var showEnterPasswordScreenJob: Job? = null

}