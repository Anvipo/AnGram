package com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory

import com.anvipo.angram.applicationLayer.navigation.router.Routable
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.AuthorizationCoordinator
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.AuthorizationCoordinatorImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.AuthorizationViewControllersFactoryImp
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.MainCoordinator
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.MainCoordinatorImp
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.screensFactory.MainScreensFactoryImp

object ApplicationCoordinatorsFactoryImp : ApplicationCoordinatorsFactory {

    override fun createMainCoordinator(
        router: Routable,
        tdLibGateway: TDLibGateway
    ): MainCoordinator = MainCoordinatorImp(
        router = router,
        screensFactory = MainScreensFactoryImp
    )

    override fun createAuthCoordinator(
        router: Routable,
        tdLibGateway: TDLibGateway
    ): AuthorizationCoordinator = AuthorizationCoordinatorImp(
        router = router,
        viewControllersFactory = AuthorizationViewControllersFactoryImp,
        tdLibGateway = tdLibGateway
    )

}