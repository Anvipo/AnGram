package com.anvipo.angram.layers.application.coordinator.coordinatorsFactory

import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces.AuthorizationCoordinator
import com.anvipo.angram.layers.presentation.flows.main.coordinator.interfaces.MainCoordinator

interface ApplicationCoordinatorsFactory {

    fun createAuthorizationCoordinator(): AuthorizationCoordinator

    suspend fun createMainCoordinator(): MainCoordinator

}