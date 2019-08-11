package com.anvipo.angram.layers.application.coordinator

import com.anvipo.angram.layers.application.coordinator.coordinatorsFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.layers.application.coordinator.types.ApplicationCoordinateResult
import com.anvipo.angram.layers.application.types.SystemMessageSendChannel
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
                myLog(
                    invokationPlace = object {}.javaClass.enclosingMethod!!.name,
                    currentParameters = "authState = $authState"
                )
            }
        }
    }

    override fun onFailureGetAuthorizationStateResult(error: Throwable) {
        myLog(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            currentParameters = "error.localizedMessage = ${error.localizedMessage}"
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
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        startAuthorizationFlow()
    }

    private fun onAuthorizationStateClosed() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        // TODO: recreate client

        checkAuthorizationStateHelper()
    }

    private fun onAuthorizationStateLoggingOut() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        checkAuthorizationStateHelper()
    }


    private fun onAuthorizationStateWaitTdlibParameters() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        myLaunch {
            val setTdLibParametersResult = tdLibGateway.setTdLibParametersCatching()

            setTdLibParametersResult
                .onSuccess { checkAuthorizationStateHelper() }
                .onFailure { checkAuthorizationStateHelper() }
        }
    }

    private fun onAuthorizationStateWaitEncryptionKey() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        myLaunch {
            val checkDatabaseEncryptionKeyResult = tdLibGateway.checkDatabaseEncryptionKeyCatching()

            checkDatabaseEncryptionKeyResult
                .onSuccess { checkAuthorizationStateHelper() }
                .onFailure { checkAuthorizationStateHelper() }
        }
    }

    private fun onAuthorizationStateReady() {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name
        myLog(invokationPlace = invokationPlace)

        // TODO: start main flow
        myLog(
            currentParameters = "START MAIN FLOW",
            invokationPlace = invokationPlace
        )
    }

    private fun onAuthorizationStateWaitCode() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        startAuthorizationFlow(withEnterAuthorizationCodeAsRootScreen = true)
    }

    private fun onAuthorizationStateWaitPhoneNumber() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        startAuthorizationFlow()
    }

    private fun startAuthorizationFlow(withEnterAuthorizationCodeAsRootScreen: Boolean = false) {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name

        myLaunch {
            val authorizationCoordinator = coordinatorsFactory.createAuthorizationCoordinator()

            if (withEnterAuthorizationCodeAsRootScreen) {
                authorizationCoordinator.startAuthorizationFlowWithEnterAuthorizationCodeAsRootScreen()
            } else {
                authorizationCoordinator.start()
            }

            // TODO: start main flow
            myLog(
                currentParameters = "START MAIN FLOW",
                invokationPlace = invokationPlace
            )
        }
    }

}
