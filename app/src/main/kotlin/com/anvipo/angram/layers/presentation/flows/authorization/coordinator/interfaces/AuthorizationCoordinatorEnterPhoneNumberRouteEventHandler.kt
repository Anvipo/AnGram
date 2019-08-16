package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces

import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.types.ProxyType

interface AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler {

    suspend fun onPressedBackButtonInEnterPhoneNumberScreen()

    suspend fun onAddProxyButtonTapped(proxyType: ProxyType)

}
