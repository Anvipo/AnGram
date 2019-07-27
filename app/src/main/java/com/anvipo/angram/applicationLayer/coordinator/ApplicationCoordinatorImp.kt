package com.anvipo.angram.applicationLayer.coordinator

import android.content.Context
import com.anvipo.angram.applicationLayer.types.SystemMessageSendChannel
import com.anvipo.angram.coreLayer.CoreHelpers.assertionFailure
import com.anvipo.angram.coreLayer.message.SystemMessage
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.global.CoreHelpers.createTGSystemMessage
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinator
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorInput
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.MainCoordinatorInput
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import kotlin.coroutines.CoroutineContext

class ApplicationCoordinatorImp(
    private val tdLibGateway: TDLibGateway,
    private val systemMessageSendChannel: SystemMessageSendChannel,
    private val authorizationCoordinator: AuthorizationCoordinatorInput,
    private val mainCoordinator: MainCoordinatorInput,
    private val context: Context
) : BaseCoordinator(), ApplicationCoordinatorInput, ApplicationCoordinatorOutput {

    override fun coldStartApp() {
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

    /**
    Configure and starts app
     */
    private fun configureApp() {
        startApp()
    }

    /**
    Starts needed flow
     */
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
            startMainFlow()
        }

        addChildCoordinator(coordinator = authorizationCoordinator)

        if (withEnterAuthorizationCodeAsRootScreen) {
            authorizationCoordinator.startAuthorizationFlowWithEnterAuthorizationCodeAsRootScreen()
        } else {
            authorizationCoordinator.coldStart()
        }
    }

    private fun startMainFlow() {
        val tag = "${this::class.java.simpleName} startMainFlow"
        systemMessageSendChannel.offer(SystemMessage(text = tag))

        mainCoordinator.finishFlow = {
            val logoutCatchingCoroutineExceptionHandler =
                CoroutineExceptionHandler { _, error ->
                    val errorText = "$tag ${error.localizedMessage}"

                    systemMessageSendChannel.offer(createTGSystemMessage(errorText))
                }

            logoutCatchingJob = launch(
                context = coroutineContext + logoutCatchingCoroutineExceptionHandler
            ) {
                val logoutResult = tdLibGateway.logoutCatching()

                logoutResult
                    .onSuccess {
                        TODO("should recreate tdlib client")
                    }
                    .onFailure {
                        checkAuthorizationState()
                    }
            }
        }

        addChildCoordinator(coordinator = mainCoordinator)

        mainCoordinator.coldStart()
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

        startMainFlow()
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
