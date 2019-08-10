package com.anvipo.angram.presentationLayer.flows.authFlow.coordinator

import com.anvipo.angram.applicationLayer.types.SystemMessageSendChannel
import com.anvipo.angram.coreLayer.CoreHelpers.assertionFailure
import com.anvipo.angram.coreLayer.CoreHelpers.debugLog
import com.anvipo.angram.coreLayer.message.SystemMessage
import com.anvipo.angram.dataLayer.gateways.tdLib.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.global.GlobalHelpers.createTGSystemMessage
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinatorImp
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.interfaces.*
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactory
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.enterPhoneNumberScreenFactory.EnterPhoneNumberScreenFactoryImp
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.types.AuthorizationCoordinateResult
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.types.ProxyType
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import ru.terrakok.cicerone.Router
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthorizationCoordinatorImp(
    private val router: Router,
    private val screensFactory: AuthorizationScreensFactory,
    private val authorizationTDLibGateway: AuthorizationTDLibGateway,
    private val systemMessageSendChannel: SystemMessageSendChannel
) : BaseCoordinatorImp<AuthorizationCoordinateResult>(),
    AuthorizationCoordinator,
    AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler,
    AuthorizationCoordinatorEnterAuthenticationCodeRouteEventHandler,
    AuthorizationCoordinatorEnterAuthenticationPasswordRouteEventHandler,
    AuthorizationCoordinatorAddProxyRouteEventHandler {

    override suspend fun start(): AuthorizationCoordinateResult = checkAuthState()

    override suspend fun startAuthorizationFlowWithEnterAuthorizationCodeAsRootScreen()
            : AuthorizationCoordinateResult =
        suspendCoroutine {
            finishAuthorizationFlow = it
            startAuthorizationFlowWithEnterAuthorizationCodeAsRootScreenHelper()
        }

    override suspend fun startAuthFlowWithEnterPasswordAsRootScreen(): AuthorizationCoordinateResult =
        suspendCoroutine {
            finishAuthorizationFlow = it
            startAuthFlowWithEnterPasswordAsRootScreenHelper()
        }

    override fun onEnterCorrectPhoneNumber() {
        val tag = "${this::class.java.simpleName} onEnterCorrectPhoneNumber"

        val text = "$tag: entered correct phone number"

        systemMessageSendChannel.offer(createLogMessage(text))

        checkAuthStateHelper()
    }

    override fun onPressedBackButtonInEnterPhoneNumberScreen() {
        val tag = "${this::class.java.simpleName} onPressedBackButtonInEnterPhoneNumberScreen"

        val text = "$tag: back button pressed in enter phone number screen"

        debugLog(text)

        router.exit()
    }

    override fun onAddProxyButtonTapped(proxyType: ProxyType) {
        val addProxyScreen = screensFactory
            .addProxyScreenFactory
            .createAddProxyScreen(
                proxyType = proxyType,
                shouldShowBackButton = true
            )

        router.navigateTo(addProxyScreen)
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

        checkAuthStateHelper()
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

        checkAuthStateHelper()
    }

    override fun onPressedBackButtonInAddProxyScreen() {
        val tag = "${this::class.java.simpleName} onPressedBackButtonInAddProxyScreen"

        val text = "$tag: back button pressed in add proxy screen"

        systemMessageSendChannel.offer(createLogMessage(text))

        router.exit()
    }

    override fun onSuccessAddProxy() {
        val tag = "${this::class.java.simpleName} onSuccessAddProxy"

        val text = "$tag: successfully added proxy in add proxy screen"

        systemMessageSendChannel.offer(createLogMessage(text))

        router.exit()
    }

    override val coroutineContext: CoroutineContext = Dispatchers.IO


    /// PRIVATE

    private lateinit var finishAuthorizationFlow: Continuation<AuthorizationCoordinateResult>

    private fun startAuthorizationFlowWithEnterAuthorizationCodeAsRootScreenHelper() {
        val getAuthorizationStateRequestCatchingCEH =
            CoroutineExceptionHandler { _, error ->
                val errorText = error.localizedMessage

                systemMessageSendChannel.offer(createTGSystemMessage(errorText))
            }

        launch(
            context = coroutineContext + getAuthorizationStateRequestCatchingCEH
        ) {
            val authorizationStateResult = authorizationTDLibGateway.getAuthorizationStateRequestCatching()

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
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    private fun startAuthFlowWithEnterPasswordAsRootScreenHelper() {
        val getAuthorizationStateRequestCatchingCEH =
            CoroutineExceptionHandler { _, error ->
                val errorText = error.localizedMessage

                systemMessageSendChannel.offer(createTGSystemMessage(errorText))
            }

        launch(
            context = coroutineContext + getAuthorizationStateRequestCatchingCEH
        ) {
            val authorizationStateResult = authorizationTDLibGateway.getAuthorizationStateRequestCatching()

            authorizationStateResult
                .onSuccess {
                    if (it is TdApi.AuthorizationStateWaitPassword) {
                        onAuthorizationStateWaitPassword(
                            withEnterAuthenticationPasswordAsRootScreen = true
                        )
                    } else {
                        onSuccessGetAuthStateResult(it)
                    }
                }
                .onFailure(::onFailureGetAuthStateResult)
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    private suspend fun checkAuthState(): AuthorizationCoordinateResult = suspendCoroutine {
        finishAuthorizationFlow = it
        checkAuthStateHelper()
    }

    private fun checkAuthStateHelper() {
        val getAuthorizationStateRequestCatchingCEH =
            CoroutineExceptionHandler { _, error ->
                val errorText = error.localizedMessage

                systemMessageSendChannel.offer(createTGSystemMessage(errorText))
            }

        launch(
            context = coroutineContext + getAuthorizationStateRequestCatchingCEH
        ) {
            val authorizationStateResult = authorizationTDLibGateway.getAuthorizationStateRequestCatching()

            authorizationStateResult
                .onSuccess(::onSuccessGetAuthStateResult)
                .onFailure(::onFailureGetAuthStateResult)
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    private fun onSuccessGetAuthStateResult(authState: TdApi.AuthorizationState) {
        showNeededScreen(authState)
    }

    private fun onFailureGetAuthStateResult(error: Throwable) {
        val tag = "${this::class.java.simpleName} onFailureGetAuthStateResult"
        val text = "$tag: ${error.localizedMessage}"

        systemMessageSendChannel.offer(SystemMessage(text = text))

        checkAuthStateHelper()
    }


    private fun showNeededScreen(authState: TdApi.AuthorizationState) {
        when (authState) {
            is TdApi.AuthorizationStateWaitPhoneNumber -> onAuthStateWaitsPhoneNumber()
            is TdApi.AuthorizationStateWaitCode -> onAuthStateWaitsCode(authState)
            is TdApi.AuthorizationStateWaitPassword -> onAuthorizationStateWaitPassword()
            is TdApi.AuthorizationStateReady -> onAuthorizationStateReady()
            else -> {
                val tag = "${this::class.java.simpleName} showNeededScreen"
                val text = "$tag: Undefined authState: $authState"
                systemMessageSendChannel.offer(createTGSystemMessage(text))
            }
        }
    }


    private fun onAuthStateWaitsPhoneNumber() {
        val showEnterPhoneNumberScreenCoroutineExceptionHandler =
            CoroutineExceptionHandler { _, error ->
                val errorText = error.localizedMessage

                systemMessageSendChannel.offer(createTGSystemMessage(errorText))
            }

        launch(
            context = Dispatchers.Main + showEnterPhoneNumberScreenCoroutineExceptionHandler
        ) { showEnterPhoneNumberScreen() }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    private fun onAuthStateWaitsCode(
        currentAuthorizationState: TdApi.AuthorizationStateWaitCode,
        withEnterAuthCodeAsRootScreen: Boolean = false
    ) {
        val showEnterAuthCodeScreenCEH =
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

        launch(
            context = Dispatchers.Main + showEnterAuthCodeScreenCEH
        ) {
            showEnterAuthCodeScreen(
                withEnterAuthCodeAsRootScreen = withEnterAuthCodeAsRootScreen,
                expectedCodeLength = expectedCodeLength,
                enteredPhoneNumber = enteredPhoneNumber,
                registrationRequired = registrationRequired,
                termsOfServiceText = termsOfServiceText
            )
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    private fun onAuthorizationStateWaitPassword(
        withEnterAuthenticationPasswordAsRootScreen: Boolean = false
    ) {
        val showEnterPasswordScreenCEH =
            CoroutineExceptionHandler { _, error ->
                val errorText = error.localizedMessage

                systemMessageSendChannel.offer(createTGSystemMessage(errorText))
            }

        launch(
            context = Dispatchers.Main + showEnterPasswordScreenCEH
        ) {
            showEnterAuthenticationPasswordScreen(
                withEnterAuthenticationPasswordAsRootScreen = withEnterAuthenticationPasswordAsRootScreen
            )
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    private fun showEnterAuthenticationPasswordScreen(withEnterAuthenticationPasswordAsRootScreen: Boolean) {
        if (withEnterAuthenticationPasswordAsRootScreen) {
            val enterPhoneNumberScreen =
                screensFactory
                    .enterPhoneNumberScreenFactory
                    .createEnterPhoneNumberScreen()

            val enterPasswordScreen =
                screensFactory
                    .enterPasswordScreenFactory
                    .createEnterPasswordScreen()

            router.newRootChain(enterPhoneNumberScreen, enterPasswordScreen)
        } else {
            val enterPasswordScreen =
                screensFactory
                    .enterPasswordScreenFactory
                    .createEnterPasswordScreen()

            router.navigateTo(enterPasswordScreen)
        }
    }

    private fun onAuthorizationStateReady() {
        val tag = "${this::class.java.simpleName} onAuthorizationStateReady"
        val text = "$tag: successful authorization"
        systemMessageSendChannel.offer(createTGSystemMessage(text))

        finishAuthorizationFlow.resume(AuthorizationCoordinateResult)
    }


    private fun showEnterPhoneNumberScreen() {
        val enterPhoneNumberScreen =
            screensFactory
                .enterPhoneNumberScreenFactory
                .createEnterPhoneNumberScreen()

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
                    .createEnterAuthCodeScreen(
                        expectedCodeLength = expectedCodeLength,
                        enteredPhoneNumber = enteredPhoneNumber,
                        registrationRequired = registrationRequired,
                        termsOfServiceText = termsOfServiceText,
                        shouldShowBackButton = true
                    )

            val enterPhoneNumberScreen =
                screensFactory
                    .enterPhoneNumberScreenFactory
                    .createEnterPhoneNumberScreen()

            router.newRootChain(enterPhoneNumberScreen, enterAuthCodeScreen)
        } else {
            val enterAuthCodeScreen =
                screensFactory
                    .enterAuthenticationCodeScreenFactory
                    .createEnterAuthCodeScreen(
                        expectedCodeLength = expectedCodeLength,
                        enteredPhoneNumber = enteredPhoneNumber,
                        registrationRequired = registrationRequired,
                        termsOfServiceText = termsOfServiceText,
                        shouldShowBackButton = true
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

}