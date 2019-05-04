package com.anvipo.angram.applicationLayer.navigation.coordinator

import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.applicationLayer.navigation.router.Routable
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.global.assertionFailure
import com.anvipo.angram.global.debugLog
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi

class ApplicationCoordinator(
    private val router: Routable,
    private val coordinatorsFactory: ApplicationCoordinatorsFactory,
    private val tdLibGateway: TDLibGateway
) : BaseCoordinator() {

    override fun start() {
        childCoordinators.clear()

        configureApp()
    }

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
        val tag = "${this::class.java.simpleName} startApp"

        GlobalScope.launch {
            val authorizationStateResult = tdLibGateway.getAuthorizationStateRequestCatching()

            authorizationStateResult
                .onSuccess(onSuccessGetAuthStateResult(tag))
                .onFailure(onFailureGetAuthStateResult(tag))
        }
    }

    private fun startAuthFlow() {
        val authCoordinator = coordinatorsFactory.createAuthCoordinator(
            router = router,
            tdLibGateway = tdLibGateway
        )

        authCoordinator.finishFlow = {
            removeChildCoordinator(coordinator = authCoordinator)
            startMainFlow()
        }

        addChildCoordinator(coordinator = authCoordinator)

        authCoordinator.start()
    }

    private fun startMainFlow() {
        val mainCoordinator = coordinatorsFactory.createMainCoordinator(
            router = router,
            tdLibGateway = tdLibGateway
        )

        addChildCoordinator(coordinator = mainCoordinator)

        mainCoordinator.start()
    }


    private fun onFailureGetAuthStateResult(tag: String): (Throwable) -> Unit = { error ->
        // TODO: handle this case

        val message = "$tag: authStateResult.onFailure: ${error.localizedMessage}"

        debugLog(message)

        assertionFailure()
    }

    private fun onSuccessGetAuthStateResult(tag: String): (TdApi.AuthorizationState) -> Unit = { authState ->
        when (authState) {
            is TdApi.AuthorizationStateWaitTdlibParameters -> {
                val message = "$tag: onSuccessGetAuthStateResult: TDLib waits parameters"

                debugLog(message)

                startAuthFlow()
            }
            else -> {
                // TODO: handle this case

                val message = "$tag: onSuccessGetAuthStateResult; TdApi.AuthorizationState: $authState"

                debugLog(message)

                assertionFailure()
            }
        }
    }

}
