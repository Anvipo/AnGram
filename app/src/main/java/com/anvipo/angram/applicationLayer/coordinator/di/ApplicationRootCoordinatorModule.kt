package com.anvipo.angram.applicationLayer.coordinator.di

import com.anvipo.angram.applicationLayer.coordinator.ApplicationCoordinatorImp
import com.anvipo.angram.applicationLayer.coordinator.ApplicationCoordinatorInput
import com.anvipo.angram.applicationLayer.di.LaunchSystemModule
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule
import com.anvipo.angram.applicationLayer.types.SystemMessageSendChannel
import com.anvipo.angram.businessLogicLayer.di.GatewaysModule
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.AuthorizationCoordinatorImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorInput
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactoryImp
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.MainCoordinatorImp
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.MainCoordinatorInput
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.screensFactory.MainViewControllersFactoryImp
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.Router

object ApplicationRootCoordinatorModule {

    internal val applicationCoordinator: StringQualifier = named("applicationCoordinator")
    internal val authorizationCoordinator: StringQualifier = named("authorizationCoordinator")
    internal val mainCoordinator: StringQualifier = named("mainCoordinator")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<ApplicationCoordinatorInput>(applicationCoordinator) {
            ApplicationCoordinatorImp(
                tdLibGateway = get<TDLibGateway>(GatewaysModule.tdLibGateway),
                systemMessageSendChannel =
                get<SystemMessageSendChannel>(LaunchSystemModule.systemMessageSendChannel),
                authorizationCoordinator =
                get<AuthorizationCoordinatorInput>(authorizationCoordinator),
                mainCoordinator = get<MainCoordinatorInput>(mainCoordinator),
                context = get()
            )
        }

        single<AuthorizationCoordinatorInput>(authorizationCoordinator) {
            AuthorizationCoordinatorImp(
                context = get(),
                router = get<Router>(SystemInfrastructureModule.router),
                screensFactory = AuthorizationScreensFactoryImp,
                systemMessageSendChannel =
                get<SystemMessageSendChannel>(LaunchSystemModule.systemMessageSendChannel),
                tdLibGateway = get<TDLibGateway>(GatewaysModule.tdLibGateway)
            )
        }

        single<MainCoordinatorInput>(mainCoordinator) {
            MainCoordinatorImp(
                router = get<Router>(SystemInfrastructureModule.router),
                viewControllersFactory = MainViewControllersFactoryImp
            )
        }

    }

}