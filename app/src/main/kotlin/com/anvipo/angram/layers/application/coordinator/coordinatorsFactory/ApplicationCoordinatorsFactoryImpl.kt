package com.anvipo.angram.layers.application.coordinator.coordinatorsFactory

import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces.AuthorizationCoordinator
import org.koin.core.scope.Scope

class ApplicationCoordinatorsFactoryImpl(
    private val koinScope: Scope
) : ApplicationCoordinatorsFactory {

    override fun createAuthorizationCoordinator(): AuthorizationCoordinator =
        koinScope.get(authorizationCoordinatorQualifier)

}