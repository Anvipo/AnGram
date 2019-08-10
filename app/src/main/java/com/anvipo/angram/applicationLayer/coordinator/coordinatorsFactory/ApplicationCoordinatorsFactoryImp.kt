package com.anvipo.angram.applicationLayer.coordinator.coordinatorsFactory

import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.interfaces.AuthorizationCoordinator
import org.koin.core.scope.Scope

@Suppress("RemoveExplicitTypeArguments")
class ApplicationCoordinatorsFactoryImp(
    private val koinScope: Scope
) : ApplicationCoordinatorsFactory {

    override fun createAuthorizationCoordinator(): AuthorizationCoordinator =
        koinScope.get<AuthorizationCoordinator>(
            authorizationCoordinatorQualifier
        )

}