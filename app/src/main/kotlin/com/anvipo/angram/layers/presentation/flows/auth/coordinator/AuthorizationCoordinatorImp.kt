package com.anvipo.angram.layers.presentation.flows.auth.coordinator

import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.global.HasCheckAuthorizationStateHelper
import com.anvipo.angram.layers.global.types.SystemMessageSendChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationState
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateReceiveChannel
import com.anvipo.angram.layers.presentation.common.baseClasses.BaseCoordinatorImp
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.interfaces.*
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.authorization.AuthorizationScreensFactory
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.types.AuthorizationCoordinateResult
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.types.ProxyType
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule
import kotlinx.coroutines.Dispatchers
import org.drinkless.td.libcore.telegram.TdApi
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import kotlin.coroutines.Continuation

@Suppress("RedundantUnitReturnType")
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

    override fun onReceivedTdApiUpdateAuthorizationState(
        receivedTdApiUpdateAuthorizationState: TdApiUpdateAuthorizationState
    ) {
        when (val authorizationState = receivedTdApiUpdateAuthorizationState.authorizationState) {
            is TdApi.AuthorizationStateWaitPhoneNumber -> onAuthStateWaitsPhoneNumber()
            is TdApi.AuthorizationStateWaitCode -> onAuthStateWaitsCode(authorizationState)
            is TdApi.AuthorizationStateWaitPassword -> onAuthorizationStateWaitPassword()
            is TdApi.AuthorizationStateReady -> onAuthorizationStateReady()
        }
    }

    override fun onPressedBackButtonInEnterPhoneNumberScreen() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        router.exit()
    }

    override fun onPressedBackButtonInEnterAuthenticationCodeScreen() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        router.exit()

        enterAuthenticationCodeScreenHasBeenPresented = false
    }

    override fun onPressedBackButtonInEnterAuthenticationPasswordScreen() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        router.backTo(enterPhoneNumberScreen)
    }

    override fun onAddProxyButtonTapped(proxyType: ProxyType) {
        myLog(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            text = "proxyType = $proxyType"
        )

        val addProxyScreen = screensFactory
            .addProxyScreenFactory
            .createAddProxyScreen(
                proxyType = proxyType,
                shouldShowBackButton = true
            )

        router.navigateTo(addProxyScreen)
    }

    override fun onPressedBackButtonInAddProxyScreen() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        router.exit()
    }

    override fun onSuccessAddProxy() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        router.exit()
    }


    private var enterPhoneNumberScreen: SupportAppScreen? = null

    private var enterAuthenticationCodeScreenHasBeenPresented = false


    private fun onAuthStateWaitsPhoneNumber() {
        showEnterPhoneNumberScreen()
    }

    private fun onAuthStateWaitsCode(
        currentAuthorizationState: TdApi.AuthorizationStateWaitCode
    ) {
        val (
            expectedCodeLength,
            enteredPhoneNumber,
            registrationRequired,
            termsOfServiceText
        ) = prepareParametersForEnterAuthCodeScreen(currentAuthorizationState)

        showEnterAuthenticationCodeScreen(
            expectedCodeLength = expectedCodeLength,
            enteredPhoneNumber = enteredPhoneNumber,
            registrationRequired = registrationRequired,
            termsOfServiceText = termsOfServiceText
        )
    }

    private fun onAuthorizationStateWaitPassword() {
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


    private fun showEnterPhoneNumberScreen() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        enterPhoneNumberScreen =
            screensFactory
                .enterPhoneNumberScreenFactory
                .createEnterPhoneNumberScreen()

        myLaunch(Dispatchers.Main) {
            router.newRootScreen(enterPhoneNumberScreen!!)
        }
    }

    private fun showEnterAuthenticationCodeScreen(
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
            EnterAuthenticationCodeModule.EnterAuthenticationCodeScreenParameters(
                expectedCodeLength = expectedCodeLength,
                enteredPhoneNumber = enteredPhoneNumber,
                registrationRequired = registrationRequired,
                termsOfServiceText = termsOfServiceText,
                shouldShowBackButton = true
            )

        val enterAuthenticationCodeScreen =
            screensFactory
                .enterAuthenticationCodeScreenFactory
                .createEnterAuthenticationCodeScreen(enterAuthenticationCodeScreenParameters)

        myLog(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            text = "Show enter authentication code screen: enterPhoneNumberScreen = $enterPhoneNumberScreen"
        )

        enterAuthenticationCodeScreenHasBeenPresented = true

        if (enterPhoneNumberScreen == null) {
            enterPhoneNumberScreen = screensFactory
                .enterPhoneNumberScreenFactory
                .createEnterPhoneNumberScreen()

            myLaunch(Dispatchers.Main) {
                router.newRootChain(enterPhoneNumberScreen!!, enterAuthenticationCodeScreen)
            }
        } else {
            myLaunch(Dispatchers.Main) {
                router.navigateTo(enterAuthenticationCodeScreen)
            }
        }
    }

    private fun showEnterAuthenticationPasswordScreen() {
        myLog(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name
        )

        val enterAuthenticationPasswordScreen =
            screensFactory
                .enterAuthenticationPasswordScreenFactory
                .createEnterPasswordScreen()

        myLog(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            text = "Show enter authentication code screen: enterPhoneNumberScreen = $enterPhoneNumberScreen"
        )

        if (enterPhoneNumberScreen == null) {
            enterPhoneNumberScreen = screensFactory
                .enterPhoneNumberScreenFactory
                .createEnterPhoneNumberScreen()

            myLaunch(Dispatchers.Main) {
                router.newRootChain(enterPhoneNumberScreen!!, enterAuthenticationPasswordScreen)
            }
        } else {
            myLaunch(Dispatchers.Main) {
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