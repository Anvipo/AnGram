package com.anvipo.angram.applicationLayer.coordinator.coordinatorsFactory

import com.anvipo.angram.presentationLayer.flows.auth.coordinator.interfaces.AuthorizationCoordinator

interface ApplicationCoordinatorsFactory {

    fun createAuthorizationCoordinator(): AuthorizationCoordinator

}