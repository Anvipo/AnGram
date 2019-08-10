package com.anvipo.angram.layers.application.coordinator.coordinatorsFactory

import com.anvipo.angram.layers.application.coordinator.di.ApplicationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.interfaces.AuthorizationCoordinator
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