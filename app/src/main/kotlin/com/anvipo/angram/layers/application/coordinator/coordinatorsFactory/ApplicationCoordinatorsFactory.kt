package com.anvipo.angram.layers.application.coordinator.coordinatorsFactory

import com.anvipo.angram.presentationLayer.flows.auth.coordinator.interfaces.AuthorizationCoordinator

interface ApplicationCoordinatorsFactory {

    fun createAuthorizationCoordinator(): AuthorizationCoordinator

}