package com.anvipo.angram.layers.application.coordinator

import com.anvipo.angram.layers.application.coordinator.coordinatorsFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.layers.application.coordinator.types.ApplicationCoordinateResult
import com.anvipo.angram.layers.application.types.SystemMessageSendChannel
import com.anvipo.angram.layers.core.CoreHelpers.debugLog
import com.anvipo.angram.layers.core.message.SystemMessage
import com.anvipo.angram.layers.data.gateways.tdLib.application.ApplicationTDLibGateway
import com.anvipo.angram.layers.global.GlobalHelpers.createTGSystemMessageWithLogging
import com.anvipo.angram.layers.presentation.common.baseClasses.BaseCoordinatorImp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.suspendCoroutine

@Suppress("RedundantUnitReturnType")
class ApplicationCoordinatorImp(
    private val coordinatorsFactory: ApplicationCoordinatorsFactory,
    private val tdLibGateway: ApplicationTDLibGateway,
    private val systemMessageSendChannel: SystemMessageSendChannel
) : BaseCoordinatorImp<ApplicationCoordinateResult>(),
    ApplicationCoordinator {

    override suspend fun start(): ApplicationCoordinateResult {
        childCoordinators.clear()

        return configureApp()
    }

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    private suspend fun configureApp(): ApplicationCoordinateResult = startApp()

    private suspend fun startApp(): ApplicationCoordinateResult = checkAuthorizationState()

    private lateinit var finishApplicationFlow: Continuation<ApplicationCoordinateResult>

    private suspend fun checkAuthorizationState(): ApplicationCoordinateResult = suspendCoroutine {
        finishApplicationFlow = it
        checkAuthorizationStateHelper()
    }

    private fun checkAuthorizationStateHelper() {
        val tag = "${this::class.java.simpleName} checkAuthorizationStateHelper"

        val getAuthorizationStateRequestCatchingCEH =
            CoroutineExceptionHandler { _, error ->
                val errorText = error.localizedMessage

                val text = "$tag $errorText"

                systemMessageSendChannel.offer(createTGSystemMessageWithLogging(text))
            }

        launch(
            context = coroutineContext + getAuthorizationStateRequestCatchingCEH
        ) {
            val authorizationStateResult = tdLibGateway.getAuthorizationStateRequestCatching()

            authorizationStateResult
                .onSuccess(::onSuccessGetAuthorizationStateResult)
                .onFailure(::onFailureGetAuthorizationStateResult)
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }


    private fun onSuccessGetAuthorizationStateResult(authState: TdApi.AuthorizationState) {
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
                val tag = "${this::class.java.simpleName} onSuccessGetAuthorizationStateResult"
                val text = "$tag: TdApi.AuthorizationState: $authState"

                systemMessageSendChannel.offer(SystemMessage(text = text))
            }
        }
    }

    private fun onFailureGetAuthorizationStateResult(error: Throwable) {
        val tag = "${this::class.java.simpleName} onFailureGetAuthorizationStateResult"
        val text = "$tag error: ${error.localizedMessage}"

        systemMessageSendChannel.offer(SystemMessage(text = text))

        checkAuthorizationStateHelper()
    }

    private fun onAuthorizationStateWaitPassword() {
        val tag = "${this::class.java.simpleName} onAuthorizationStateWaitPassword"

        systemMessageSendChannel.offer(SystemMessage(text = tag))

        startAuthorizationFlow()
    }

    private fun onAuthorizationStateClosed() {
        val tag = "${this::class.java.simpleName} onAuthorizationStateClosed"

        systemMessageSendChannel.offer(SystemMessage(text = tag))

        checkAuthorizationStateHelper()
    }

    private fun onAuthorizationStateLoggingOut() {
        val tag = "${this::class.java.simpleName} onAuthorizationStateLoggingOut"

        systemMessageSendChannel.offer(SystemMessage(text = tag))

        checkAuthorizationStateHelper()
    }


    private fun onAuthorizationStateWaitTdlibParameters() {
        val tag = "${this::class.java.simpleName} onAuthorizationStateWaitTdlibParameters"
        val text = "$tag onAuthorizationStateWaitTdlibParameters: TDLib waits parameters"
        systemMessageSendChannel.offer(SystemMessage(text = text))

        val setTdLibParametersCatchingCEH =
            CoroutineExceptionHandler { _, error ->
                val errorText = "$tag ${error.localizedMessage}"

                systemMessageSendChannel.offer(createTGSystemMessageWithLogging(errorText))
            }

        launch(
            context = coroutineContext + setTdLibParametersCatchingCEH
        ) {
            val setTdLibParametersResult = tdLibGateway.setTdLibParametersCatching()

            setTdLibParametersResult
                .onSuccess { checkAuthorizationStateHelper() }
                .onFailure { checkAuthorizationStateHelper() }
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    private fun onAuthorizationStateWaitEncryptionKey() {
        val tag = "${this::class.java.simpleName} onAuthorizationStateWaitEncryptionKey"
        val text = "$tag onAuthorizationStateWaitEncryptionKey: TDLib waits encryption key"
        systemMessageSendChannel.offer(SystemMessage(text = text))

        val checkDatabaseEncryptionKeyCatchingCEH =
            CoroutineExceptionHandler { _, error ->
                val errorText = error.localizedMessage

                systemMessageSendChannel.offer(createTGSystemMessageWithLogging(errorText))
            }

        launch(
            context = coroutineContext + checkDatabaseEncryptionKeyCatchingCEH
        ) {
            val setTdLibParametersResult = tdLibGateway.checkDatabaseEncryptionKeyCatching()

            setTdLibParametersResult
                .onSuccess { checkAuthorizationStateHelper() }
                .onFailure { checkAuthorizationStateHelper() }
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

    private fun onAuthorizationStateReady() {
        val tag = "${this::class.java.simpleName} onAuthorizationStateReady"

        systemMessageSendChannel.offer(SystemMessage(text = tag))

        // TODO: start main flow
        debugLog("start main flow")
    }

    private fun onAuthorizationStateWaitCode() {
        val tag = "${this::class.java.simpleName} onAuthorizationStateWaitCode"

        systemMessageSendChannel.offer(SystemMessage(text = tag))

        startAuthorizationFlow(withEnterAuthorizationCodeAsRootScreen = true)
    }

    private fun onAuthorizationStateWaitPhoneNumber() {
        val tag = "${this::class.java.simpleName} onAuthorizationStateWaitPhoneNumber"

        systemMessageSendChannel.offer(SystemMessage(text = tag))

        startAuthorizationFlow()
    }

    private fun startAuthorizationFlow(withEnterAuthorizationCodeAsRootScreen: Boolean = false) {
        val startAuthenticationFlowCEH =
            CoroutineExceptionHandler { _, error ->
                val errorText = error.localizedMessage

                systemMessageSendChannel.offer(createTGSystemMessageWithLogging(errorText))
            }

        launch(coroutineContext + startAuthenticationFlowCEH) {
            val authorizationCoordinator = coordinatorsFactory.createAuthorizationCoordinator()

            if (withEnterAuthorizationCodeAsRootScreen) {
                authorizationCoordinator.startAuthorizationFlowWithEnterAuthorizationCodeAsRootScreen()
            } else {
                authorizationCoordinator.start()
            }

            // TODO: start main flow
            debugLog("start main flow")
        }.also { jobsThatWillBeCancelledInOnDestroy += it }
    }

}
