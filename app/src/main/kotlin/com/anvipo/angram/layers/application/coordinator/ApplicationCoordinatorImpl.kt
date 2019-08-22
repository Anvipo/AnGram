package com.anvipo.angram.layers.application.coordinator

import com.anvipo.angram.layers.application.coordinator.coordinatorsFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.layers.application.coordinator.types.ApplicationCoordinateResult
import com.anvipo.angram.layers.core.CoreHelpers.IS_IN_DEBUG_MODE
import com.anvipo.angram.layers.core.base.classes.BaseCoordinatorImpl
import com.anvipo.angram.layers.data.gateways.tdLib.application.ApplicationTDLibGateway
import com.anvipo.angram.layers.global.HasCheckAuthorizationStateHelper
import com.anvipo.angram.layers.global.types.SystemMessageSendChannel
import com.anvipo.angram.layers.global.types.TdApiUpdateAuthorizationStateReceiveChannel
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScopeQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.di.EnterAuthenticationPasswordModule
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.di.EnterAuthenticationPhoneNumberModule
import com.anvipo.angram.layers.presentation.flows.main.coordinator.di.MainCoordinatorModule
import com.anvipo.angram.layers.presentation.flows.main.coordinator.di.MainCoordinatorModule.mainCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.main.coordinator.di.MainCoordinatorModule.mainCoordinatorScopeQualifier
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.di.ChatListModule
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.di.PrivateChatsModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.scope.Scope
import kotlin.coroutines.Continuation

@Suppress("RedundantUnitReturnType")
class ApplicationCoordinatorImpl(
    private val coordinatorsFactory: ApplicationCoordinatorsFactory,
    private val tdLibGateway: ApplicationTDLibGateway,
    private val tdApiUpdateAuthorizationStateReceiveChannel: TdApiUpdateAuthorizationStateReceiveChannel,
    private val koinScope: Scope,
    systemMessageSendChannel: SystemMessageSendChannel
) : BaseCoordinatorImpl<ApplicationCoordinateResult>(
    systemMessageSendChannel = systemMessageSendChannel
),
    ApplicationCoordinator,
    HasCheckAuthorizationStateHelper<ApplicationCoordinateResult> {

    override var startAuthorizationState: TdApi.UpdateAuthorizationState? = null

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

    @ExperimentalCoroutinesApi
    override suspend fun onReceivedTdApiUpdateAuthorizationState(
        receivedUpdateAuthorizationState: TdApi.UpdateAuthorizationState
    ) {
        when (receivedUpdateAuthorizationState.authorizationState) {
            is TdApi.AuthorizationStateWaitTdlibParameters ->
                onAuthorizationStateWaitTdlibParameters()
            is TdApi.AuthorizationStateWaitEncryptionKey -> onAuthorizationStateWaitEncryptionKey()
            is TdApi.AuthorizationStateWaitPhoneNumber ->
                onAuthorizationStateWaitPhoneNumber(receivedUpdateAuthorizationState)
            is TdApi.AuthorizationStateWaitCode ->
                onAuthorizationStateWaitCode(receivedUpdateAuthorizationState)
            is TdApi.AuthorizationStateWaitPassword ->
                onAuthorizationStateWaitPassword(receivedUpdateAuthorizationState)
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

    @ExperimentalCoroutinesApi
    private suspend fun onAuthorizationStateWaitPhoneNumber(
        startAuthorizationState: TdApi.UpdateAuthorizationState
    ) {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        startAuthorizationFlow(startAuthorizationState)
    }

    @ExperimentalCoroutinesApi
    private suspend fun onAuthorizationStateWaitCode(
        startAuthorizationState: TdApi.UpdateAuthorizationState
    ) {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        startAuthorizationFlow(startAuthorizationState)
    }

    @ExperimentalCoroutinesApi
    private suspend fun onAuthorizationStateWaitPassword(
        startAuthorizationState: TdApi.UpdateAuthorizationState
    ) {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        startAuthorizationFlow(startAuthorizationState)
    }

    private suspend fun onAuthorizationStateReady() {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name
        myLog(invokationPlace = invokationPlace)

        startMainFlow()
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

    @ExperimentalCoroutinesApi
    private suspend fun startAuthorizationFlow(startAuthorizationState: TdApi.UpdateAuthorizationState) {
        if (authorizationFlowHasBeenStarted) {
            return
        }

        authorizationFlowHasBeenStarted = true

        val authorizationFlowModules by lazy {
            if (IS_IN_DEBUG_MODE) {
                listOf(
                    AuthorizationCoordinatorModule.module,
                    EnterAuthenticationPhoneNumberModule.module,
                    EnterAuthenticationCodeModule.module,
                    EnterAuthenticationPasswordModule.module
                )
            } else {
                TODO("release config")
            }
        }

        loadKoinModules(authorizationFlowModules)

        val invokationPlace = object {}.javaClass.enclosingMethod!!.name

        authorizationCoordinatorScope = koinScope.getKoin().createScope(
            scopeId = "Authorization flow scope ID",
            qualifier = authorizationCoordinatorScopeQualifier
        )

        val authorizationCoordinator = coordinatorsFactory.createAuthorizationCoordinator()

        authorizationCoordinator.set(startAuthorizationState)

        val authorizationFlowCoordinateResult = coordinateTo(authorizationCoordinator)

        myLog(
            text = "authorizationFlowCoordinateResult = $authorizationFlowCoordinateResult",
            invokationPlace = invokationPlace
        )

        authorizationCoordinatorScope!!.close()

        unloadKoinModules(authorizationFlowModules)

        authorizationFlowHasBeenStarted = false

        startMainFlow()
    }

    private suspend fun startMainFlow() {
        val mainFlowModules by lazy {
            if (IS_IN_DEBUG_MODE) {
                listOf(
                    MainCoordinatorModule.module,
                    ChatListModule.module,
                    PrivateChatsModule.module
                )
            } else {
                TODO("release config")
            }
        }

        loadKoinModules(mainFlowModules)

        val invokationPlace = object {}.javaClass.enclosingMethod!!.name

        mainCoordinatorScope = koinScope.getKoin().createScope(
            scopeId = "Main flow scope ID",
            qualifier = mainCoordinatorScopeQualifier
        )

        val mainCoordinator = withContext(Dispatchers.Default) {
            coordinatorsFactory.createMainCoordinator()
        }

        val mainFlowCoordinateResult = coordinateTo(mainCoordinator)

        myLog(
            text = "mainFlowCoordinateResult = $mainFlowCoordinateResult",
            invokationPlace = invokationPlace
        )

        mainCoordinatorScope.close()

        unloadKoinModules(mainFlowModules)
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
