package com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory

import com.anvipo.angram.applicationLayer.navigation.router.Routable
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.AuthorizationCoordinator
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.AuthorizationCoordinatorImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.AuthorizationScreensFactoryImp

class ApplicationCoordinatorFactoryImp : ApplicationCoordinatorFactory {

    override fun createAuthCoordinator(router: Routable): AuthorizationCoordinator {
        return AuthorizationCoordinatorImp(
            router = router,
            screensFactory = AuthorizationScreensFactoryImp()
        )
    }

}