package com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory

import android.content.Context
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.coreLayer.collections.IReadOnlyStack
import com.anvipo.angram.coreLayer.message.ISentDataNotifier
import com.anvipo.angram.coreLayer.message.SystemMessage
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.AuthorizationCoordinator
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.AuthorizationCoordinatorImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactory
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.MainCoordinator
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.MainCoordinatorImp
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.screensFactory.MainViewControllersFactoryImp
import org.drinkless.td.libcore.telegram.TdApi
import ru.terrakok.cicerone.Router

class ApplicationCoordinatorsFactoryImp(
    private val context: Context,
    private val systemMessageNotifier: ISentDataNotifier<SystemMessage>,
    private val authorizationScreensFactory: AuthorizationScreensFactory,
    private val tdUpdateAuthorizationStateStack: IReadOnlyStack<TdApi.UpdateAuthorizationState>
) : ApplicationCoordinatorsFactory {

    override fun createMainCoordinator(
        router: Router,
        tdLibGateway: TDLibGateway
    ): MainCoordinator = MainCoordinatorImp(
        router = router,
        viewControllersFactory = MainViewControllersFactoryImp
    )

    override fun createAuthCoordinator(
        router: Router,
        tdLibGateway: TDLibGateway
    ): AuthorizationCoordinator = AuthorizationCoordinatorImp(
        context = context,
        router = router,
        screensFactory = authorizationScreensFactory,
        tdLibGateway = tdLibGateway,
        tdUpdateAuthorizationStateStack = tdUpdateAuthorizationStateStack,
        systemMessageNotifier = systemMessageNotifier
    )

}