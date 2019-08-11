package com.anvipo.angram.layers.application.coordinator

import com.anvipo.angram.layers.application.coordinator.coordinatorsFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.layers.application.coordinator.types.ApplicationCoordinateResult
import com.anvipo.angram.layers.application.types.SystemMessageSendChannel
import com.anvipo.angram.layers.core.CoreHelpers.debugLog
import com.anvipo.angram.layers.data.gateways.tdLib.application.ApplicationTDLibGateway
import com.anvipo.angram.layers.presentation.common.baseClasses.BaseCoordinatorWithCheckAuthorizationStateHelpers
import org.drinkless.td.libcore.telegram.TdApi
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine

@Suppress("RedundantUnitReturnType")
class ApplicationCoordinatorImp(
    private val coordinatorsFactory: ApplicationCoordinatorsFactory,
    private val tdLibGateway: ApplicationTDLibGateway,
    systemMessageSendChannel: SystemMessageSendChannel
) : BaseCoordinatorWithCheckAuthorizationStateHelpers<ApplicationCoordinateResult>(
    tdLibGateway = tdLibGateway,
    systemMessageSendChannel = systemMessageSendChannel
),
    ApplicationCoordinator {

    override suspend fun start(): ApplicationCoordinateResult {
        childCoordinators.clear()

        return configureApp()
    }


    override fun onSuccessGetAuthorizationStateResult(authState: TdApi.AuthorizationState) {
        when (authState) {
            is TdApi.AuthorizationStateWaitTdlibParameters -> onAuthorizationStateWaitTdlibParameters()
            is TdApi.AuthorizationStateWaitEncryptionKey -> onAuthorizationStateWaitEncryptionKey()
            is TdApi.AuthorizationStateWaitPhoneNumber -> onAuthorizationStateWaitPhoneNumber()
            is TdApi.AuthorizationStateWaitCode -> onAuthorizationStateWaitCode()
            is TdApi.AuthorizationStateWaitPassword -> onAuthorizationStateWaitPassword()
            is TdApi.AuthorizationStateReady -> onAuthorizationStateReady()
            is TdApi.AuthorizationStateLoggingOut -> onAuthorizationStateLoggingOut()
            is TdApi.AuthorizationStateClosed -> onAuthorizationStateClosed()
            else -> {
                log(
                    invokationPlace = object {}.javaClass.enclosingMethod!!.name,
                    text = "authState = $authState"
                )
            }
        }
    }

    override fun onFailureGetAuthorizationStateResult(error: Throwable) {
        log(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            text = "error.localizedMessage = ${error.localizedMessage}"
        )

        checkAuthorizationStateHelper()
    }


    private suspend fun configureApp(): ApplicationCoordinateResult = startApp()

    private suspend fun startApp(): ApplicationCoordinateResult = checkAuthorizationState()

    private lateinit var finishApplicationFlow: Continuation<ApplicationCoordinateResult>

    private suspend fun checkAuthorizationState(): ApplicationCoordinateResult = suspendCoroutine {
        finishApplicationFlow = it
        checkAuthorizationStateHelper()
    }


    private fun onAuthorizationStateWaitPassword() {
        log(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        startAuthorizationFlow()
    }

    private fun onAuthorizationStateClosed() {
        log(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        // TODO: recreate client

        checkAuthorizationStateHelper()
    }

    private fun onAuthorizationStateLoggingOut() {
        log(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        checkAuthorizationStateHelper()
    }


    private fun onAuthorizationStateWaitTdlibParameters() {
        log(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        myLaunch {
            val setTdLibParametersResult = tdLibGateway.setTdLibParametersCatching()

            setTdLibParametersResult
                .onSuccess { checkAuthorizationStateHelper() }
                .onFailure { checkAuthorizationStateHelper() }
        }
    }

    private fun onAuthorizationStateWaitEncryptionKey() {
        log(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        myLaunch {
            val checkDatabaseEncryptionKeyResult = tdLibGateway.checkDatabaseEncryptionKeyCatching()

            checkDatabaseEncryptionKeyResult
                .onSuccess { checkAuthorizationStateHelper() }
                .onFailure { checkAuthorizationStateHelper() }
        }
    }

    private fun onAuthorizationStateReady() {
        log(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        // TODO: start main flow
        debugLog("start main flow")
    }

    private fun onAuthorizationStateWaitCode() {
        log(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        startAuthorizationFlow(withEnterAuthorizationCodeAsRootScreen = true)
    }

    private fun onAuthorizationStateWaitPhoneNumber() {
        log(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        startAuthorizationFlow()
    }

    private fun startAuthorizationFlow(withEnterAuthorizationCodeAsRootScreen: Boolean = false) {
        myLaunch {
            val authorizationCoordinator = coordinatorsFactory.createAuthorizationCoordinator()

            if (withEnterAuthorizationCodeAsRootScreen) {
                authorizationCoordinator.startAuthorizationFlowWithEnterAuthorizationCodeAsRootScreen()
            } else {
                authorizationCoordinator.start()
            }

            // TODO: start main flow
            debugLog("start main flow")
        }
    }

}
