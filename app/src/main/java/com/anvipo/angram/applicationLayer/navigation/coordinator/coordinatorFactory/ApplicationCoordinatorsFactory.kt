package com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory

import com.anvipo.angram.applicationLayer.navigation.router.Routable
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.AuthorizationCoordinator
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.MainCoordinator

interface ApplicationCoordinatorsFactory {

    fun createAuthCoordinator(
        router: Routable,
        tdLibGateway: TDLibGateway
    ): AuthorizationCoordinator

    fun createMainCoordinator(
        router: Routable,
        tdLibGateway: TDLibGateway
    ): MainCoordinator

}