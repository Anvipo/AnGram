package com.anvipo.angram.layers.presentation.flows.auth.coordinator

import com.anvipo.angram.layers.application.types.SystemMessageSendChannel
import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.data.gateways.tdLib.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.layers.presentation.common.baseClasses.BaseCoordinatorWithCheckAuthorizationStateHelpers
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.interfaces.*
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.authorization.AuthorizationScreensFactory
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.types.AuthorizationCoordinateResult
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.types.ProxyType
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule
import org.drinkless.td.libcore.telegram.TdApi
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthorizationCoordinatorImp(
    private val router: Router,
    private val screensFactory: AuthorizationScreensFactory,
    private val tdLibGateway: AuthorizationTDLibGateway,
    systemMessageSendChannel: SystemMessageSendChannel
) : BaseCoordinatorWithCheckAuthorizationStateHelpers<AuthorizationCoordinateResult>(
    tdLibGateway = tdLibGateway,
    systemMessageSendChannel = systemMessageSendChannel
),
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
            startAuthorizationFlowWithEnterAuthorizationPasswordAsRootScreenHelper()
        }

    override fun onEnterCorrectPhoneNumber() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        checkAuthorizationStateHelper()
    }

    override fun onPressedBackButtonInEnterPhoneNumberScreen() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        router.exit()
    }

    override fun onAddProxyButtonTapped(proxyType: ProxyType) {
        myLog(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            currentParameters = "proxyType = $proxyType"
        )

        val addProxyScreen = screensFactory
            .addProxyScreenFactory
            .createAddProxyScreen(
                proxyType = proxyType,
                shouldShowBackButton = true
            )

        router.navigateTo(addProxyScreen)
    }

    override fun onPressedBackButtonInEnterAuthenticationCodeScreen() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        router.exit()
    }

    override fun onEnterCorrectAuthenticationCode() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        checkAuthorizationStateHelper()
    }

    override fun onPressedBackButtonInEnterAuthenticationPasswordScreen() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        router.backTo(enterPhoneNumberScreen)
    }

    override fun onEnterCorrectAuthenticationPassword() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        checkAuthorizationStateHelper()
    }

    override fun onPressedBackButtonInAddProxyScreen() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        router.exit()
    }

    override fun onSuccessAddProxy() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        router.exit()
    }

    override fun onSuccessGetAuthorizationStateResult(authState: TdApi.AuthorizationState) {
        myLog(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            currentParameters = "authState = $authState"
        )

        showNeededScreen(authState)
    }

    override fun onFailureGetAuthorizationStateResult(error: Throwable) {
        myLog(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            currentParameters = "error.localizedMessage = ${error.localizedMessage}"
        )

        checkAuthorizationStateHelper()
    }


    private lateinit var finishAuthorizationFlow: Continuation<AuthorizationCoordinateResult>

    private var enterPhoneNumberScreen: SupportAppScreen? = null

    private fun startAuthorizationFlowWithEnterAuthorizationCodeAsRootScreenHelper() {
        checkAuthorizationStateHelperWithGeneric<TdApi.AuthorizationStateWaitCode> {
            onAuthStateWaitsCode(
                it,
                withEnterAuthCodeAsRootScreen = true
            )
        }
    }

    private fun startAuthorizationFlowWithEnterAuthorizationPasswordAsRootScreenHelper() {
        checkAuthorizationStateHelperWithGeneric<TdApi.AuthorizationStateWaitPassword> {
            onAuthorizationStateWaitPassword(
                withEnterAuthenticationPasswordAsRootScreen = true
            )
        }
    }

    private suspend fun checkAuthState(): AuthorizationCoordinateResult = suspendCoroutine {
        finishAuthorizationFlow = it
        checkAuthorizationStateHelper()
    }


    private fun showNeededScreen(authState: TdApi.AuthorizationState) {
        when (authState) {
            is TdApi.AuthorizationStateWaitPhoneNumber -> onAuthStateWaitsPhoneNumber()
            is TdApi.AuthorizationStateWaitCode -> onAuthStateWaitsCode(authState)
            is TdApi.AuthorizationStateWaitPassword -> onAuthorizationStateWaitPassword()
            is TdApi.AuthorizationStateReady -> onAuthorizationStateReady()
            else -> {
                myLog(
                    invokationPlace = object {}.javaClass.enclosingMethod!!.name,
                    currentParameters = "Undefined authState: $authState"
                )
            }
        }
    }


    private fun onAuthStateWaitsPhoneNumber() {
        showScreenHelper {
            showEnterPhoneNumberScreen()
        }
    }

    private fun onAuthStateWaitsCode(
        currentAuthorizationState: TdApi.AuthorizationStateWaitCode,
        withEnterAuthCodeAsRootScreen: Boolean = false
    ) {
        showScreenHelper {
            val (
                expectedCodeLength,
                enteredPhoneNumber,
                registrationRequired,
                termsOfServiceText
            ) = prepareParametersForEnterAuthCodeScreen(currentAuthorizationState)

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
        withEnterAuthenticationPasswordAsRootScreen: Boolean = false
    ) {
        showScreenHelper {
            showEnterAuthenticationPasswordScreen(withEnterAuthenticationPasswordAsRootScreen)
        }
    }

    private fun showEnterAuthenticationPasswordScreen(withEnterAuthenticationPasswordAsRootScreen: Boolean) {
        val text = "withEnterAuthenticationPasswordAsRootScreen = $withEnterAuthenticationPasswordAsRootScreen"

        myLog(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            currentParameters = text
        )

        if (withEnterAuthenticationPasswordAsRootScreen) {
            val enterPhoneNumberScreen =
                screensFactory
                    .enterPhoneNumberScreenFactory
                    .createEnterPhoneNumberScreen()

            val enterPasswordScreen =
                screensFactory
                    .enterAuthenticationPasswordScreenFactory
                    .createEnterPasswordScreen()

            router.newRootChain(enterPhoneNumberScreen, enterPasswordScreen)
        } else {
            val enterPasswordScreen =
                screensFactory
                    .enterAuthenticationPasswordScreenFactory
                    .createEnterPasswordScreen()

            router.navigateTo(enterPasswordScreen)
        }
    }

    private fun onAuthorizationStateReady() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        finishAuthorizationFlow.resume(AuthorizationCoordinateResult)
    }


    private fun showEnterPhoneNumberScreen() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        enterPhoneNumberScreen =
            screensFactory
                .enterPhoneNumberScreenFactory
                .createEnterPhoneNumberScreen()

        router.newRootScreen(enterPhoneNumberScreen!!)
    }

    private fun showEnterAuthCodeScreen(
        withEnterAuthCodeAsRootScreen: Boolean = false,
        expectedCodeLength: Int = 5,
        enteredPhoneNumber: String = "",
        registrationRequired: Boolean,
        termsOfServiceText: String
    ) {
        myLog(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            currentParameters = "withEnterAuthCodeAsRootScreen = $withEnterAuthCodeAsRootScreen"
        )

        val enterAuthenticationCodeScreenParameters =
            EnterAuthenticationCodeModule.EnterAuthenticationCodeScreenParameters(
                expectedCodeLength = expectedCodeLength,
                enteredPhoneNumber = enteredPhoneNumber,
                registrationRequired = registrationRequired,
                termsOfServiceText = termsOfServiceText,
                shouldShowBackButton = true
            )

        if (withEnterAuthCodeAsRootScreen) {
            val enterAuthCodeScreen =
                screensFactory
                    .enterAuthenticationCodeScreenFactory
                    .createEnterAuthenticationCodeScreen(enterAuthenticationCodeScreenParameters)

            val enterPhoneNumberScreen =
                screensFactory
                    .enterPhoneNumberScreenFactory
                    .createEnterPhoneNumberScreen()

            router.newRootChain(enterPhoneNumberScreen, enterAuthCodeScreen)
        } else {
            val enterAuthCodeScreen =
                screensFactory
                    .enterAuthenticationCodeScreenFactory
                    .createEnterAuthenticationCodeScreen(enterAuthenticationCodeScreenParameters)

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
            is TdApi.AuthenticationCodeTypeFlashCall -> 0
            else -> {
                assertionFailure("Undefined codeInfoType")
                0
            }
        }

        val registrationRequired = !currentAuthorizationState.isRegistered
        val termsOfServiceText: String = if (registrationRequired) {
            val termsOfService = currentAuthorizationState.termsOfService

            if (termsOfService != null) {
                termsOfService.text.text
            } else {
                ""
            }
        } else {
            ""
        }

        return EnterAuthCodeScreenParameters(
            expectedCodeLength = expectedCodeLength,
            enteredPhoneNumber = codeInfo.phoneNumber,
            registrationRequired = registrationRequired,
            termsOfServiceText = termsOfServiceText
        )
    }


    private inline fun <reified T : TdApi.AuthorizationState> checkAuthorizationStateHelperWithGeneric(
        noinline onFailure: ((Throwable) -> Unit)? = null,
        noinline onSuccess: ((T) -> Unit)? = null
    ) {
        myLaunch {
            val authorizationStateResult = tdLibGateway.getAuthorizationStateCatching()

            authorizationStateResult
                .onSuccess {
                    if (onSuccess != null && it is T) {
                        onSuccess.invoke(it)
                    } else {
                        onSuccessGetAuthorizationStateResult(it)
                    }
                }
                .onFailure {
                    if (onFailure != null) {
                        onFailure.invoke(it)
                    } else {
                        onFailureGetAuthorizationStateResult(it)
                    }
                }
        }
    }

}