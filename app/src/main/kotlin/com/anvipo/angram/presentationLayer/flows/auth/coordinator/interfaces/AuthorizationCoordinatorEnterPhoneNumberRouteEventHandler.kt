package com.anvipo.angram.presentationLayer.flows.auth.coordinator.interfaces

import com.anvipo.angram.presentationLayer.flows.auth.screens.addProxy.types.ProxyType

interface AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler {

    fun onPressedBackButtonInEnterPhoneNumberScreen()
    fun onEnterCorrectPhoneNumber()

    fun onAddProxyButtonTapped(proxyType: ProxyType)

}
