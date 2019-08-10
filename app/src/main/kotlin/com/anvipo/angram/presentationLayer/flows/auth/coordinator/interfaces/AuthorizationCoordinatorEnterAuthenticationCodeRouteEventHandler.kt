package com.anvipo.angram.presentationLayer.flows.auth.coordinator.interfaces

interface AuthorizationCoordinatorEnterAuthenticationCodeRouteEventHandler {

    fun onPressedBackButtonInEnterAuthenticationCodeScreen()
    fun onEnterCorrectAuthenticationCode()

}