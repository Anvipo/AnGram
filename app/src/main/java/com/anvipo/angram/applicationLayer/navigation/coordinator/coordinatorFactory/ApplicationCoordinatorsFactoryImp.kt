package com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory

import com.anvipo.angram.applicationLayer.navigation.router.Routable
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.AuthorizationCoordinator
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.AuthorizationCoordinatorImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.AuthorizationViewControllersFactoryImp
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.MainCoordinator
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.MainCoordinatorImp
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.screensFactory.MainScreensFactoryImp

class ApplicationCoordinatorsFactoryImp : ApplicationCoordinatorsFactory {

    override fun createMainCoordinator(router: Routable): MainCoordinator {
        return MainCoordinatorImp(
            router = router,
            screensFactory = MainScreensFactoryImp()
        )
    }

    override fun createAuthCoordinator(router: Routable): AuthorizationCoordinator {
        return AuthorizationCoordinatorImp(
            router = router,
            viewControllersFactory = AuthorizationViewControllersFactoryImp()
        )
    }

}