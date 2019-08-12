package com.anvipo.angram.layers.presentation.flows.auth.coordinator.interfaces

import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.types.ProxyType

interface AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler {

    fun onPressedBackButtonInEnterPhoneNumberScreen()

    fun onAddProxyButtonTapped(proxyType: ProxyType)

}
