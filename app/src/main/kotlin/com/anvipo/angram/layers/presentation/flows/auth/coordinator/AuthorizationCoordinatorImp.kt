package com.anvipo.angram.layers.presentation.flows.auth.coordinator

import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.core.base.classes.BaseCoordinatorImp
import com.anvipo.angram.layers.global.HasCheckAuthorizationStateHelper
import com.anvipo.angram.layers.global.types.SystemMessageSendChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationState
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateReceiveChannel
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.interfaces.*
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.authorization.AuthorizationScreensFactory
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.types.AuthorizationCoordinateResult
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.types.ProxyType
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.TdApi
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import kotlin.coroutines.Continuation

class AuthorizationCoordinatorImp(
    private val router: Router,
    private val screensFactory: AuthorizationScreensFactory,
    private val tdApiUpdateAuthorizationStateReceiveChannel: TdApiUpdateAuthorizationStateReceiveChannel,
    systemMessageSendChannel: SystemMessageSendChannel
) : BaseCoordinatorImp<AuthorizationCoordinateResult>(
    systemMessageSendChannel = systemMessageSendChannel
),
    AuthorizationCoordinator,
    AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler,
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

    override suspend fun onPressedBackButtonInEnterPhoneNumberScreen() {
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
            router.backTo(enterPhoneNumberScreen)
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


    private var enterPhoneNumberScreen: SupportAppScreen? = null

    private var enterAuthenticationCodeScreenHasBeenPresented = false


    private suspend fun onAuthStateWaitsPhoneNumber() {
        showEnterPhoneNumberScreen()
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


    private suspend fun showEnterPhoneNumberScreen() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        enterPhoneNumberScreen = withContext(Dispatchers.Default) {
            screensFactory
                .enterPhoneNumberScreenFactory
                .createEnterPhoneNumberScreen()
        }

        withContext(Dispatchers.Main) {
            router.newRootScreen(enterPhoneNumberScreen!!)
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
            // TODO: set new parameters in authentication screen
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
            text = "Show enter authentication code screen: enterPhoneNumberScreen = $enterPhoneNumberScreen"
        )

        enterAuthenticationCodeScreenHasBeenPresented = true

        if (enterPhoneNumberScreen == null) {
            enterPhoneNumberScreen = withContext(Dispatchers.Default) {
                screensFactory
                    .enterPhoneNumberScreenFactory
                    .createEnterPhoneNumberScreen()
            }

            withContext(Dispatchers.Main) {
                router.newRootChain(
                    enterPhoneNumberScreen!!,
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
            text = "Show enter authentication code screen: enterPhoneNumberScreen = $enterPhoneNumberScreen"
        )

        if (enterPhoneNumberScreen == null) {
            enterPhoneNumberScreen = withContext(Dispatchers.Default) {
                screensFactory
                    .enterPhoneNumberScreenFactory
                    .createEnterPhoneNumberScreen()
            }

            withContext(Dispatchers.Main) {
                router.newRootChain(enterPhoneNumberScreen!!, enterAuthenticationPasswordScreen)
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