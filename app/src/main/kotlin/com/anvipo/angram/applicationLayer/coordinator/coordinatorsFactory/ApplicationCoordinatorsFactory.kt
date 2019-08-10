package com.anvipo.angram.applicationLayer.coordinator.coordinatorsFactory

import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.interfaces.AuthorizationCoordinator

interface ApplicationCoordinatorsFactory {

    fun createAuthorizationCoordinator(): AuthorizationCoordinator

}