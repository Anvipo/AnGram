package com.anvipo.angram.layers.presentation.flows.authorization.coordinator

import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.core.base.classes.BaseCoordinatorImpl
import com.anvipo.angram.layers.global.HasCheckAuthorizationStateHelper
import com.anvipo.angram.layers.global.types.SystemMessageSendChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationState
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateReceiveChannel
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces.*
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.authorization.AuthorizationScreensFactory
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.types.AuthorizationCoordinateResult
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.types.ProxyType
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.TdApi
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import kotlin.coroutines.Continuation

class AuthorizationCoordinatorImpl(
    private val router: Router,
    private val screensFactory: AuthorizationScreensFactory,
    private val tdApiUpdateAuthorizationStateReceiveChannel: TdApiUpdateAuthorizationStateReceiveChannel,
    systemMessageSendChannel: SystemMessageSendChannel
) : BaseCoordinatorImpl<AuthorizationCoordinateResult>(
    systemMessageSendChannel = systemMessageSendChannel
),
    AuthorizationCoordinator,
    AuthorizationCoordinatorEnterAuthenticationPhoneNumberRouteEventHandler,
    AuthorizationCoordinatorEnterAuthenticationCodeRouteEventHandler,
    AuthorizationCoordinatorEnterAuthenticationPasswordRouteEventHandler,
    AuthorizationCoordinatorAddProxyRouteEventHandler,
    HasCheckAuthorizationStateHelper<AuthorizationCoordinateResult> {

    override suspend fun start(): AuthorizationCoordinateResult =
        checkAuthorizationState(tdApiUpdateAuthorizationStateReceiveChannel)

    override fun onCreatedCheckAuthorizationStateContinuation(
        checkAuthorizationStateContinuation: Continuation<AuthorizationCoordinateResult>
    ) {
        finishFlowContinuation = checkAuthorizationStateContinuation
    }

    override suspend fun onReceivedTdApiUpdateAuthorizationState(
        receivedTdApiUpdateAuthorizationState: TdApiUpdateAuthorizationState
    ) {
        when (val authorizationState = receivedTdApiUpdateAuthorizationState.authorizationState) {
            is TdApi.AuthorizationStateWaitPhoneNumber -> onAuthStateWaitsPhoneNumber()
            is TdApi.AuthorizationStateWaitCode -> onAuthStateWaitsCode(authorizationState)
            is TdApi.AuthorizationStateWaitPassword -> onAuthorizationStateWaitPassword()
            is TdApi.AuthorizationStateReady -> onAuthorizationStateReady()
        }
    }

    override suspend fun onPressedBackButtonInEnterAuthenticationPhoneNumberScreen() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        withContext(Dispatchers.Main) {
            router.exit()
        }
    }

    override suspend fun onPressedBackButtonInEnterAuthenticationCodeScreen() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        withContext(Dispatchers.Main) {
            router.exit()
        }

        enterAuthenticationCodeScreenHasBeenPresented = false
    }

    override suspend fun onPressedBackButtonInEnterAuthenticationPasswordScreen() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        withContext(Dispatchers.Main) {
            router.backTo(enterAuthenticationPhoneNumberScreen)
        }
    }

    override suspend fun onAddProxyButtonTapped(proxyType: ProxyType) {
        myLog(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            text = "proxyType = $proxyType"
        )

        val addProxyScreen = withContext(Dispatchers.Default) {
            screensFactory
                .addProxyScreenFactory
                .createAddProxyScreen(
                    proxyType = proxyType,
                    shouldShowBackButton = true
                )
        }

        withContext(Dispatchers.Main) {
            router.navigateTo(addProxyScreen)
        }
    }

    override suspend fun onPressedBackButtonInAddProxyScreen() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        withContext(Dispatchers.Main) {
            router.exit()
        }
    }

    override suspend fun onSuccessAddProxy() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        withContext(Dispatchers.Main) {
            router.exit()
        }
    }


    private var enterAuthenticationPhoneNumberScreen: SupportAppScreen? = null

    private var enterAuthenticationCodeScreenHasBeenPresented = false


    private suspend fun onAuthStateWaitsPhoneNumber() {
        showEnterAuthenticationPhoneNumberScreen()
    }

    private suspend fun onAuthStateWaitsCode(
        currentAuthorizationState: TdApi.AuthorizationStateWaitCode
    ) {
        val (
            expectedCodeLength,
            enteredPhoneNumber,
            registrationRequired,
            termsOfServiceText
        ) = withContext(Dispatchers.Default) {
            prepareParametersForEnterAuthCodeScreen(currentAuthorizationState)
        }

        showEnterAuthenticationCodeScreen(
            expectedCodeLength = expectedCodeLength,
            enteredPhoneNumber = enteredPhoneNumber,
            registrationRequired = registrationRequired,
            termsOfServiceText = termsOfServiceText
        )
    }

    private suspend fun onAuthorizationStateWaitPassword() {
        showEnterAuthenticationPasswordScreen()
    }

    private fun onAuthorizationStateReady() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        finishFlow(
            tdApiUpdateAuthorizationStateReceiveChannel,
            finishFlowContinuation,
            AuthorizationCoordinateResult
        )
    }


    private suspend fun showEnterAuthenticationPhoneNumberScreen() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        enterAuthenticationPhoneNumberScreen = withContext(Dispatchers.Default) {
            screensFactory
                .enterAuthenticationPhoneNumberScreenFactory
                .createEnterAuthenticationPhoneNumberScreen()
        }

        withContext(Dispatchers.Main) {
            router.newRootScreen(enterAuthenticationPhoneNumberScreen!!)
        }
    }

    private suspend fun showEnterAuthenticationCodeScreen(
        expectedCodeLength: Int = 5,
        enteredPhoneNumber: String = "",
        registrationRequired: Boolean,
        termsOfServiceText: String
    ) {
        myLog(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name
        )

        if (enterAuthenticationCodeScreenHasBeenPresented) {
            // TODO: set new parameters in enter authentication code screen
            return
        }

        val enterAuthenticationCodeScreenParameters =
            withContext(Dispatchers.Default) {
                EnterAuthenticationCodeModule.EnterAuthenticationCodeScreenParameters(
                    expectedCodeLength = expectedCodeLength,
                    enteredPhoneNumber = enteredPhoneNumber,
                    registrationRequired = registrationRequired,
                    termsOfServiceText = termsOfServiceText,
                    shouldShowBackButton = true
                )
            }

        val enterAuthenticationCodeScreen = withContext(Dispatchers.Default) {
            screensFactory
                .enterAuthenticationCodeScreenFactory
                .createEnterAuthenticationCodeScreen(enterAuthenticationCodeScreenParameters)
        }

        myLog(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            text = "Show enter authentication code screen: enterAuthenticationPhoneNumberScreen = $enterAuthenticationPhoneNumberScreen"
        )

        enterAuthenticationCodeScreenHasBeenPresented = true

        if (enterAuthenticationPhoneNumberScreen == null) {
            enterAuthenticationPhoneNumberScreen = withContext(Dispatchers.Default) {
                screensFactory
                    .enterAuthenticationPhoneNumberScreenFactory
                    .createEnterAuthenticationPhoneNumberScreen()
            }

            withContext(Dispatchers.Main) {
                router.newRootChain(
                    enterAuthenticationPhoneNumberScreen!!,
                    enterAuthenticationCodeScreen
                )
            }
        } else {
            withContext(Dispatchers.Main) {
                router.navigateTo(enterAuthenticationCodeScreen)
            }
        }
    }

    private suspend fun showEnterAuthenticationPasswordScreen() {
        myLog(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name
        )

        val enterAuthenticationPasswordScreen =
            withContext(Dispatchers.Default) {
                screensFactory
                    .enterAuthenticationPasswordScreenFactory
                    .createEnterPasswordScreen()
            }

        myLog(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            text = "Show enter authentication code screen: enterAuthenticationPhoneNumberScreen = $enterAuthenticationPhoneNumberScreen"
        )

        if (enterAuthenticationPhoneNumberScreen == null) {
            enterAuthenticationPhoneNumberScreen = withContext(Dispatchers.Default) {
                screensFactory
                    .enterAuthenticationPhoneNumberScreenFactory
                    .createEnterAuthenticationPhoneNumberScreen()
            }

            withContext(Dispatchers.Main) {
                router.newRootChain(enterAuthenticationPhoneNumberScreen!!, enterAuthenticationPasswordScreen)
            }
        } else {
            withContext(Dispatchers.Main) {
                router.navigateTo(enterAuthenticationPasswordScreen)
            }
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

}