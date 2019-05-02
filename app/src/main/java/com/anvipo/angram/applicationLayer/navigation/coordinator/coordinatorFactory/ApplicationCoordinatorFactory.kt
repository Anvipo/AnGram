package com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory

import com.anvipo.angram.applicationLayer.navigation.router.Routable
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.AuthorizationCoordinator

interface ApplicationCoordinatorFactory {

    fun createAuthCoordinator(router: Routable): AuthorizationCoordinator

}