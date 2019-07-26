package com.anvipo.angram.applicationLayer.coordinator.di

import com.anvipo.angram.applicationLayer.coordinator.ApplicationCoordinatorImp
import com.anvipo.angram.applicationLayer.coordinator.ApplicationCoordinatorInput
import com.anvipo.angram.applicationLayer.di.LaunchSystemModule.systemMessageSendChannelQualifier
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.routerQualifier
import com.anvipo.angram.applicationLayer.types.SystemMessageSendChannel
import com.anvipo.angram.dataLayer.di.GatewaysModule.tdLibGatewayQualifier
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.AuthorizationCoordinatorImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorInput
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactoryImp
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.MainCoordinatorImp
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.MainCoordinatorInput
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.screensFactory.MainViewControllersFactoryImp
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.Router

object ApplicationRootCoordinatorModule {

    internal val applicationCoordinatorQualifier = named("applicationCoordinator")
    internal val authorizationCoordinatorQualifier = named("authorizationCoordinator")
    @Suppress("MemberVisibilityCanBePrivate")
    internal val mainCoordinatorQualifier = named("mainCoordinator")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<ApplicationCoordinatorInput>(applicationCoordinatorQualifier) {
            ApplicationCoordinatorImp(
                tdLibGateway = get<TDLibGateway>(tdLibGatewayQualifier),
                systemMessageSendChannel =
                get<SystemMessageSendChannel>(systemMessageSendChannelQualifier),
                authorizationCoordinator =
                get<AuthorizationCoordinatorInput>(authorizationCoordinatorQualifier),
                mainCoordinator = get<MainCoordinatorInput>(mainCoordinatorQualifier),
                context = get()
            )
        }

        single<AuthorizationCoordinatorInput>(authorizationCoordinatorQualifier) {
            AuthorizationCoordinatorImp(
                context = get(),
                router = get<Router>(routerQualifier),
                screensFactory = AuthorizationScreensFactoryImp,
                systemMessageSendChannel =
                get<SystemMessageSendChannel>(systemMessageSendChannelQualifier),
                tdLibGateway = get<TDLibGateway>(tdLibGatewayQualifier)
            )
        }

        single<MainCoordinatorInput>(mainCoordinatorQualifier) {
            MainCoordinatorImp(
                router = get<Router>(routerQualifier),
                viewControllersFactory = MainViewControllersFactoryImp
            )
        }

    }

}