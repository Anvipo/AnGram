package com.anvipo.angram.applicationLayer.coordinator

import android.content.Context
import com.anvipo.angram.applicationLayer.types.SystemMessageSendChannel
import com.anvipo.angram.coreLayer.CoreHelpers.assertionFailure
import com.anvipo.angram.coreLayer.message.SystemMessage
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.application.ApplicationTDLibGateway
import com.anvipo.angram.global.GlobalHelpers.createTGSystemMessage
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinatorImp
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.interfaces.AuthorizationCoordinator
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import kotlin.coroutines.CoroutineContext

class ApplicationCoordinatorImp(
    private val authorizationCoordinator: AuthorizationCoordinator,
    private val tdLibGateway: ApplicationTDLibGateway,
    private val systemMessageSendChannel: SystemMessageSendChannel,
    private val context: Context
) : BaseCoordinatorImp(),
    ApplicationCoordinator {

    override fun coldStart() {
        childCoordinators.clear()

        configureApp()
    }

    override fun cancelAllJobs() {
        getAuthorizationStateRequestCatchingJob?.cancel()
        setTdLibParametersCatchingJob?.cancel()
        checkDatabaseEncryptionKeyCatchingJob?.cancel()
        logoutCatchingJob?.cancel()
    }

    override var finishFlow: (() -> Unit)? = null

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    private fun configureApp() {
        startApp()
    }

    private fun startApp() {
        checkAuthorizationState()
    }

    private fun checkAuthorizationState() {
        val tag = "${this::class.java.simpleName} checkAuthorizationState"

        val getAuthorizationStateRequestCatchingCoroutineExceptionHandler =
            CoroutineExceptionHandler { _, error ->
                val errorText = error.localizedMessage

                val text = "$tag $errorText"

                systemMessageSendChannel.offer(createTGSystemMessage(text))
            }

        getAuthorizationStateRequestCatchingJob = launch(
            context = coroutineContext + getAuthorizationStateRequestCatchingCoroutineExceptionHandler
        ) {
            val authorizationStateResult = tdLibGateway.getAuthorizationStateRequestCatching()

            authorizationStateResult
                .onSuccess(::onSuccessGetAuthorizationStateResult)
                .onFailure(::onFailureGetAuthorizationStateResult)
        }
    }

    private fun startAuthorizationFlow(withEnterAuthorizationCodeAsRootScreen: Boolean = false) {
        authorizationCoordinator.finishFlow = {
            removeChildCoordinator(coordinator = authorizationCoordinator)
            TODO()
        }

        addChildCoordinator(coordinator = authorizationCoordinator)

        if (withEnterAuthorizationCodeAsRootScreen) {
            authorizationCoordinator.startAuthorizationFlowWithEnterAuthorizationCodeAsRootScreen()
        } else {
            authorizationCoordinator.coldStart()
        }
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

                assertionFailure("Undefined authState")
            }
        }
    }

    private fun onAuthorizationStateWaitPassword() {
        val tag = "${this::class.java.simpleName} onAuthorizationStateWaitPassword"

        systemMessageSendChannel.offer(SystemMessage(text = tag))

        startAuthorizationFlow()
    }

    private fun onAuthorizationStateClosed() {
        val tag = "${this::class.java.simpleName} onAuthorizationStateClosed"

        systemMessageSendChannel.offer(SystemMessage(text = tag))

        checkAuthorizationState()
    }

    private fun onAuthorizationStateLoggingOut() {
        val tag = "${this::class.java.simpleName} onAuthorizationStateLoggingOut"

        systemMessageSendChannel.offer(SystemMessage(text = tag))

        checkAuthorizationState()
    }

    private fun onFailureGetAuthorizationStateResult(error: Throwable) {
        val tag = "${this::class.java.simpleName} onFailureGetAuthorizationStateResult"
        val text = "$tag error: ${error.localizedMessage}"

        systemMessageSendChannel.offer(SystemMessage(text = text))

        checkAuthorizationState()
    }


    private fun onAuthorizationStateWaitTdlibParameters() {
        val tag = "${this::class.java.simpleName} onAuthorizationStateWaitTdlibParameters"
        val text = "$tag onAuthorizationStateWaitTdlibParameters: TDLib waits parameters"
        systemMessageSendChannel.offer(SystemMessage(text = text))

        val setTdLibParametersCatchingCoroutineExceptionHandler =
            CoroutineExceptionHandler { _, error ->
                val errorText = "$tag ${error.localizedMessage}"

                systemMessageSendChannel.offer(createTGSystemMessage(errorText))
            }

        setTdLibParametersCatchingJob = launch(
            context = coroutineContext + setTdLibParametersCatchingCoroutineExceptionHandler
        ) {
            val setTdLibParametersResult = tdLibGateway.setTdLibParametersCatching(context)

            setTdLibParametersResult
                .onSuccess {
                    checkAuthorizationState()
                }
                .onFailure {
                    checkAuthorizationState()
                }
        }
    }

    private fun onAuthorizationStateWaitEncryptionKey() {
        val tag = "${this::class.java.simpleName} onAuthorizationStateWaitEncryptionKey"
        val text = "$tag onAuthorizationStateWaitEncryptionKey: TDLib waits encryption key"
        systemMessageSendChannel.offer(SystemMessage(text = text))

        val checkDatabaseEncryptionKeyCatchingCoroutineExceptionHandler =
            CoroutineExceptionHandler { _, error ->
                val errorText = error.localizedMessage

                systemMessageSendChannel.offer(createTGSystemMessage(errorText))
            }

        checkDatabaseEncryptionKeyCatchingJob = launch(
            context = coroutineContext + checkDatabaseEncryptionKeyCatchingCoroutineExceptionHandler
        ) {
            val setTdLibParametersResult = tdLibGateway.checkDatabaseEncryptionKeyCatching()

            setTdLibParametersResult
                .onSuccess {
                    checkAuthorizationState()
                }
                .onFailure {
                    checkAuthorizationState()
                }
        }
    }

    private fun onAuthorizationStateReady() {
        val tag = "${this::class.java.simpleName} onAuthorizationStateReady"

        systemMessageSendChannel.offer(SystemMessage(text = tag))

        TODO()
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


    private var getAuthorizationStateRequestCatchingJob: Job? = null
    private var setTdLibParametersCatchingJob: Job? = null
    private var checkDatabaseEncryptionKeyCatchingJob: Job? = null
    private var logoutCatchingJob: Job? = null

}
