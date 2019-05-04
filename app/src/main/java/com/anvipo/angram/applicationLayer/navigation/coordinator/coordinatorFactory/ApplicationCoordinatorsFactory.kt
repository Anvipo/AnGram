package com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory

import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.AuthorizationCoordinator
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.MainCoordinator
import ru.terrakok.cicerone.Router

interface ApplicationCoordinatorsFactory {

    fun createAuthCoordinator(
        router: Router,
        tdLibGateway: TDLibGateway
    ): AuthorizationCoordinator

    fun createMainCoordinator(
        router: Router,
        tdLibGateway: TDLibGateway
    ): MainCoordinator

}