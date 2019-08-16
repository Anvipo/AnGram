package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces

interface AuthorizationCoordinatorAddProxyRouteEventHandler {

    suspend fun onPressedBackButtonInAddProxyScreen()

    suspend fun onSuccessAddProxy()

}