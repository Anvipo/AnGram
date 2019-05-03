package com.anvipo.angram.applicationLayer.navigation.coordinator

import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorFactory
import com.anvipo.angram.applicationLayer.navigation.router.Routable
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinator

class ApplicationCoordinator(
    private val router: Routable,
    private val coordinatorFactory: ApplicationCoordinatorFactory
) : BaseCoordinator() {

    override var finishFlow: (() -> Unit)? = null

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
        val authCoordinator = coordinatorFactory.createAuthCoordinator(router = router)

        authCoordinator.finishFlow = {
            removeChildCoordinator(coordinator = authCoordinator)
            startMainFlow()
        }

        addChildCoordinator(coordinator = authCoordinator)

        authCoordinator.start()
    }

    private fun startMainFlow() {
        TODO()
    }

}