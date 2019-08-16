package com.anvipo.angram.layers.application.coordinator

import com.anvipo.angram.layers.application.coordinator.coordinatorsFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.layers.application.coordinator.types.ApplicationCoordinateResult
import com.anvipo.angram.layers.core.base.classes.BaseCoordinatorImpl
import com.anvipo.angram.layers.data.gateways.tdLib.application.ApplicationTDLibGateway
import com.anvipo.angram.layers.global.HasCheckAuthorizationStateHelper
import com.anvipo.angram.layers.global.types.SystemMessageSendChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationState
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateReceiveChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.TdApi
import kotlin.coroutines.Continuation

@Suppress("RedundantUnitReturnType")
class ApplicationCoordinatorImpl(
    private val coordinatorsFactory: ApplicationCoordinatorsFactory,
    private val tdLibGateway: ApplicationTDLibGateway,
    private val tdApiUpdateAuthorizationStateReceiveChannel: TdApiUpdateAuthorizationStateReceiveChannel,
    systemMessageSendChannel: SystemMessageSendChannel
) : BaseCoordinatorImpl<ApplicationCoordinateResult>(
    systemMessageSendChannel = systemMessageSendChannel
),
    ApplicationCoordinator,
    HasCheckAuthorizationStateHelper<ApplicationCoordinateResult> {

    override suspend fun start(): ApplicationCoordinateResult {
        withContext(Dispatchers.Default) {
            clearChildCoordinators()
        }

        return withContext(Dispatchers.Main) { configureApp() }
    }

    override fun onCreatedCheckAuthorizationStateContinuation(
        checkAuthorizationStateContinuation: Continuation<ApplicationCoordinateResult>
    ) {
        finishFlowContinuation = checkAuthorizationStateContinuation
    }

    override suspend fun onReceivedTdApiUpdateAuthorizationState(
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

    private suspend fun onAuthorizationStateWaitTdlibParameters() {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name
        myLog(invokationPlace = invokationPlace)

        withContext(Dispatchers.IO) {
            val setTdLibParametersResult = tdLibGateway.setTdLibParametersCatching()

            myLog(
                invokationPlace = invokationPlace,
                text = "setTdLibParametersResult = $setTdLibParametersResult"
            )
        }
    }

    private suspend fun onAuthorizationStateWaitEncryptionKey() {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name
        myLog(invokationPlace = invokationPlace)

        withContext(Dispatchers.IO) {
            val checkDatabaseEncryptionKeyResult = tdLibGateway.checkDatabaseEncryptionKeyCatching()

            myLog(
                invokationPlace = invokationPlace,
                text = "checkDatabaseEncryptionKeyResult = $checkDatabaseEncryptionKeyResult"
            )
        }
    }

    private suspend fun onAuthorizationStateWaitPhoneNumber() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        startAuthorizationFlow()
    }

    private suspend fun onAuthorizationStateWaitCode() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        startAuthorizationFlow()
    }

    private suspend fun onAuthorizationStateWaitPassword() {
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

    private suspend fun startAuthorizationFlow() {
        if (authorizationFlowHasBeenStarted) {
            return
        }

        val invokationPlace = object {}.javaClass.enclosingMethod!!.name

        val authorizationCoordinator = withContext(Dispatchers.Default) {
            coordinatorsFactory.createAuthorizationCoordinator()
        }

        authorizationFlowHasBeenStarted = true

        val authorizationFlowCoordinateResult = coordinateTo(authorizationCoordinator)

        myLog(
            text = "authorizationFlowCoordinateResult = $authorizationFlowCoordinateResult",
            invokationPlace = invokationPlace
        )

        authorizationFlowHasBeenStarted = false

        // TODO: start main flow
    }

    @Suppress("unused")
    private suspend fun logout() {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name

        withContext(Dispatchers.IO) {
            val logoutResult = tdLibGateway.logoutCatching()

            logoutResult
                .onSuccess {
                    finishFlow(
                        tdApiUpdateAuthorizationStateReceiveChannel,
                        finishFlowContinuation,
                        ApplicationCoordinateResult
                    )
                }
                .onFailure {
                    myLog(
                        invokationPlace = invokationPlace,
                        text = "throwable = $it"
                    )
                }
        }
    }

}
