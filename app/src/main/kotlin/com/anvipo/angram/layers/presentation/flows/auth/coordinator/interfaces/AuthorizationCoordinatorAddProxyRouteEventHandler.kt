package com.anvipo.angram.layers.presentation.flows.auth.coordinator.interfaces

interface AuthorizationCoordinatorAddProxyRouteEventHandler {

    suspend fun onPressedBackButtonInAddProxyScreen()

    suspend fun onSuccessAddProxy()

}