package com.anvipo.angram.layers.application.coordinator

import com.anvipo.angram.layers.application.coordinator.coordinatorsFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.layers.application.coordinator.types.ApplicationCoordinateResult
import com.anvipo.angram.layers.data.gateways.tdLib.application.ApplicationTDLibGateway
import com.anvipo.angram.layers.global.HasCheckAuthorizationStateHelper
import com.anvipo.angram.layers.global.types.SystemMessageSendChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationState
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateReceiveChannel
import com.anvipo.angram.layers.presentation.common.baseClasses.BaseCoordinatorImp
import org.drinkless.td.libcore.telegram.TdApi
import kotlin.coroutines.Continuation

@Suppress("RedundantUnitReturnType")
class ApplicationCoordinatorImp(
    private val coordinatorsFactory: ApplicationCoordinatorsFactory,
    private val tdLibGateway: ApplicationTDLibGateway,
    private val tdApiUpdateAuthorizationStateReceiveChannel: TdApiUpdateAuthorizationStateReceiveChannel,
    systemMessageSendChannel: SystemMessageSendChannel
) : BaseCoordinatorImp<ApplicationCoordinateResult>(
    systemMessageSendChannel = systemMessageSendChannel
),
    ApplicationCoordinator,
    HasCheckAuthorizationStateHelper<ApplicationCoordinateResult> {

    override suspend fun start(): ApplicationCoordinateResult {
        childCoordinators.clear()

        return configureApp()
    }

    override fun onCreatedCheckAuthorizationStateContinuation(
        checkAuthorizationStateContinuation: Continuation<ApplicationCoordinateResult>
    ) {
        finishFlowContinuation = checkAuthorizationStateContinuation
    }

    override fun onReceivedTdApiUpdateAuthorizationState(
        receivedTdApiUpdateAuthorizationState: TdApiUpdateAuthorizationState
    ) {
        when (receivedTdApiUpdateAuthorizationState.authorizationState) {
            is TdApi.AuthorizationStateWaitTdlibParameters -> onAuthorizationStateWaitTdlibParameters()
            is TdApi.AuthorizationStateWaitEncryptionKey -> onAuthorizationStateWaitEncryptionKey()
            is TdApi.AuthorizationStateWaitPhoneNumber -> onAuthorizationStateWaitPhoneNumber()
            is TdApi.AuthorizationStateWaitCode -> onAuthorizationStateWaitCode()
            is TdApi.AuthorizationStateWaitPassword -> onAuthorizationStateWaitPassword()
            is TdApi.AuthorizationStateReady -> onAuthorizationStateReady()
            is TdApi.AuthorizationStateLoggingOut -> onAuthorizationStateLoggingOut()
            is TdApi.AuthorizationStateClosing -> onAuthorizationStateClosing()
            is TdApi.AuthorizationStateClosed -> onAuthorizationStateClosed()
        }
    }


    private var authorizationFlowHasBeenStarted = false

    private suspend fun configureApp(): ApplicationCoordinateResult = startApp()

    private suspend fun startApp(): ApplicationCoordinateResult =
        checkAuthorizationState(tdApiUpdateAuthorizationStateReceiveChannel)

    private fun onAuthorizationStateWaitTdlibParameters() {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name
        myLog(invokationPlace = invokationPlace)

        myLaunch {
            val setTdLibParametersResult = tdLibGateway.setTdLibParametersCatching()

            myLog(
                invokationPlace = invokationPlace,
                text = "setTdLibParametersResult = $setTdLibParametersResult"
            )
        }
    }

    private fun onAuthorizationStateWaitEncryptionKey() {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name
        myLog(invokationPlace = invokationPlace)

        myLaunch {
            val checkDatabaseEncryptionKeyResult = tdLibGateway.checkDatabaseEncryptionKeyCatching()

            myLog(
                invokationPlace = invokationPlace,
                text = "checkDatabaseEncryptionKeyResult = $checkDatabaseEncryptionKeyResult"
            )
        }
    }

    private fun onAuthorizationStateWaitPhoneNumber() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        startAuthorizationFlow()
    }

    private fun onAuthorizationStateWaitCode() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        startAuthorizationFlow()
    }

    private fun onAuthorizationStateWaitPassword() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        startAuthorizationFlow()
    }

    private fun onAuthorizationStateReady() {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name
        myLog(invokationPlace = invokationPlace)

        // TODO: start main flow
        myLog(
            text = "START MAIN FLOW",
            invokationPlace = invokationPlace
        )
    }

    private fun onAuthorizationStateLoggingOut() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)
    }

    private fun onAuthorizationStateClosing() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)
    }

    private fun onAuthorizationStateClosed() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        finishFlow(
            tdApiUpdateAuthorizationStateReceiveChannel,
            finishFlowContinuation,
            ApplicationCoordinateResult
        )
    }

    private fun startAuthorizationFlow() {
        if (authorizationFlowHasBeenStarted) {
            return
        }

        val invokationPlace = object {}.javaClass.enclosingMethod!!.name

        myLaunch {
            val authorizationCoordinator = coordinatorsFactory.createAuthorizationCoordinator()

            authorizationFlowHasBeenStarted = true

            val authenticationFlowCoordinateResult = coordinateTo(authorizationCoordinator)

            myLog(
                text = "authenticationFlowCoordinateResult = $authenticationFlowCoordinateResult",
                invokationPlace = invokationPlace
            )

            authorizationFlowHasBeenStarted = false

            // TODO: start main flow
        }
    }

    private fun logout() {
        myLaunch {
            tdLibGateway
                .logoutCatching()
                .onSuccess {
                    finishFlow(
                        tdApiUpdateAuthorizationStateReceiveChannel,
                        finishFlowContinuation,
                        ApplicationCoordinateResult
                    )
                }
                .onFailure {
                    println()
                }
        }
    }

}
