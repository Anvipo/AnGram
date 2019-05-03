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
            val authStateResult = tdLibGateway.getAuthStateRequestCatching()

            authStateResult.onSuccess { authorizationTdApiObject ->
                when (authorizationTdApiObject) {
                    is TdApi.AuthorizationStateWaitTdlibParameters -> {
                        val message = "$tag: TdApi.AuthorizationStateWaitTdlibParameters"

                        debugLog(message)

                        startAuthFlow()
                    }
                    else -> {
                        val message = "$tag: TdApi.AuthorizationTdApiObject"

                        debugLog(message)

                        assertionFailure()
                    }
                }
            }

            authStateResult.onFailure {
                val message = "$tag: authStateResult.onFailure"

                debugLog(message)

                assertionFailure()
            }
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

}
