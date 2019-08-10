package com.anvipo.angram.applicationLayer.coordinator.coordinatorsFactory

import com.anvipo.angram.applicationLayer.di.LaunchSystemModule.systemMessageSendChannelQualifier
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.routerQualifier
import com.anvipo.angram.applicationLayer.types.SystemMessageSendChannel
import com.anvipo.angram.dataLayer.di.GatewaysModule.authorizationTDLibGatewayQualifier
import com.anvipo.angram.dataLayer.gateways.tdLib.authorization.AuthorizationTDLibGateway
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.AuthorizationCoordinatorImp
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.interfaces.AuthorizationCoordinator
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.authorizationScreensFactory.AuthorizationScreensFactoryImp
import org.koin.core.scope.Scope
import ru.terrakok.cicerone.Router

@Suppress("RemoveExplicitTypeArguments")
class ApplicationCoordinatorsFactoryImp(
    private val koinScope: Scope
) : ApplicationCoordinatorsFactory {

    override fun createAuthorizationCoordinator(): AuthorizationCoordinator =
        AuthorizationCoordinatorImp(
            router = koinScope.get<Router>(routerQualifier),
            screensFactory = AuthorizationScreensFactoryImp,
            authorizationTDLibGateway = koinScope.get<AuthorizationTDLibGateway>(
                authorizationTDLibGatewayQualifier
            ),
            systemMessageSendChannel = koinScope.get<SystemMessageSendChannel>(
                systemMessageSendChannelQualifier
            )
        )

}