package com.anvipo.angram.applicationLayer.navigation.coordinator

import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.applicationLayer.navigation.router.Routable
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinator

class ApplicationCoordinator(
    private val router: Routable,
    private val coordinatorsFactory: ApplicationCoordinatorsFactory
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
        startAuthFlow()
    }

    private fun startAuthFlow() {
        val authCoordinator = coordinatorsFactory.createAuthCoordinator(router = router)

        authCoordinator.finishFlow = {
            removeChildCoordinator(coordinator = authCoordinator)
            startMainFlow()
        }

        addChildCoordinator(coordinator = authCoordinator)

        authCoordinator.start()
    }

    private fun startMainFlow() {
        val mainCoordinator = coordinatorsFactory.createMainCoordinator(router = router)

        addChildCoordinator(coordinator = mainCoordinator)

        mainCoordinator.start()
    }

}