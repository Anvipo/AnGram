package com.anvipo.angram.layers.application.coordinator.coordinatorsFactory

import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces.AuthorizationCoordinator

interface ApplicationCoordinatorsFactory {

    fun createAuthorizationCoordinator(): AuthorizationCoordinator

}