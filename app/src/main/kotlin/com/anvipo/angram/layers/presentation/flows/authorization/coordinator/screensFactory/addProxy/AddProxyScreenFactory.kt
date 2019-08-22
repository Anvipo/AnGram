package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.addProxy

import com.anvipo.angram.layers.presentation.screens.addProxy.types.ProxyType
import com.anvipo.angram.layers.presentation.screens.addProxy.view.navigation.AddProxyScreen

interface AddProxyScreenFactory {

    fun createAddProxyScreen(
        proxyType: ProxyType,
        shouldShowBackButton: Boolean
    ): AddProxyScreen

}