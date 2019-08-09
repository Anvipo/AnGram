package com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.interfaces

import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.types.ProxyType

interface AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler {

    fun onPressedBackButtonInEnterPhoneNumberScreen()
    fun onEnterCorrectPhoneNumber()

    fun onAddProxyButtonTapped(proxyType: ProxyType)

}
