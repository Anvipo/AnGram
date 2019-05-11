package com.anvipo.angram.layers.application.coordinator.coordinatorsFactory

import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces.AuthorizationCoordinator
import com.anvipo.angram.layers.presentation.flows.main.coordinator.di.MainCoordinatorModule.mainCoordinatorQualifier
import com.anvipo.angram.layers.presentation.flows.main.coordinator.di.MainCoordinatorModule.mainCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.main.coordinator.interfaces.MainCoordinator

object ApplicationCoordinatorsFactoryImpl : ApplicationCoordinatorsFactory {

    override fun createAuthorizationCoordinator(): AuthorizationCoordinator =
        authorizationCoordinatorScope!!.get(authorizationCoordinatorQualifier)

    override suspend fun createMainCoordinator(): MainCoordinator =
        mainCoordinatorScope.get(mainCoordinatorQualifier)

}