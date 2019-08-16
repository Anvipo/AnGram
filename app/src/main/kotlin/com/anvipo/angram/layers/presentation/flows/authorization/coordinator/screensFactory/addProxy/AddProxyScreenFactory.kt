package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.addProxy

import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.types.ProxyType
import ru.terrakok.cicerone.android.support.SupportAppScreen

interface AddProxyScreenFactory {

    fun createAddProxyScreen(
        proxyType: ProxyType,
        shouldShowBackButton: Boolean
    ): SupportAppScreen

}