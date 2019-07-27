package com.anvipo.angram.applicationLayer.coordinator.di

import com.anvipo.angram.applicationLayer.coordinator.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.coordinator.ApplicationCoordinatorImp
import com.anvipo.angram.applicationLayer.di.LaunchSystemModule.systemMessageSendChannelQualifier
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.routerQualifier
import com.anvipo.angram.applicationLayer.types.SystemMessageSendChannel
import com.anvipo.angram.dataLayer.di.GatewaysModule.applicationTDLibGatewayQualifier
import com.anvipo.angram.dataLayer.di.GatewaysModule.authorizationTDLibGatewayQualifier
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.application.ApplicationTDLibGateway
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.AuthorizationCoordinatorImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinator
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactoryImp
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.MainCoordinatorImp
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.interfaces.MainCoordinator
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.screensFactory.MainScreensFactoryImp
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.Router

object ApplicationCoordinatorModule {

    internal val applicationCoordinatorQualifier = named("applicationCoordinator")
    internal val authorizationCoordinatorQualifier = named("authorizationCoordinator")
    @Suppress("MemberVisibilityCanBePrivate")
    internal val mainCoordinatorQualifier = named("mainCoordinator")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<ApplicationCoordinator>(applicationCoordinatorQualifier) {
            ApplicationCoordinatorImp(
                tdLibGateway = get<ApplicationTDLibGateway>(applicationTDLibGatewayQualifier),
                systemMessageSendChannel =
                get<SystemMessageSendChannel>(systemMessageSendChannelQualifier),
                authorizationCoordinator =
                get<AuthorizationCoordinator>(authorizationCoordinatorQualifier),
                mainCoordinator = get<MainCoordinator>(mainCoordinatorQualifier),
                context = get()
            )
        }

        single<AuthorizationCoordinator>(authorizationCoordinatorQualifier) {
            AuthorizationCoordinatorImp(
                router = get<Router>(routerQualifier),
                screensFactory = AuthorizationScreensFactoryImp,
                tdLibGateway = get<AuthorizationTDLibGateway>(authorizationTDLibGatewayQualifier),
                systemMessageSendChannel =
                get<SystemMessageSendChannel>(systemMessageSendChannelQualifier)
            )
        }

        single<MainCoordinator>(mainCoordinatorQualifier) {
            MainCoordinatorImp(
                router = get<Router>(routerQualifier),
                screensFactory = MainScreensFactoryImp
            )
        }

    }

}