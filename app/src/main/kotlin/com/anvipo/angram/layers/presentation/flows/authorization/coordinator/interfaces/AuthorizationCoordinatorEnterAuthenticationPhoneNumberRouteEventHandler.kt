package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.interfaces

import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.types.ProxyType

interface AuthorizationCoordinatorEnterAuthenticationPhoneNumberRouteEventHandler {

    suspend fun onPressedBackButtonInEnterAuthenticationPhoneNumberScreen()

    suspend fun onAddProxyButtonTapped(proxyType: ProxyType)

}
