package com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.addProxy

import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.types.ProxyType
import ru.terrakok.cicerone.android.support.SupportAppScreen

interface AddProxyScreenFactory {

    fun createAddProxyScreen(
        proxyType: ProxyType,
        shouldShowBackButton: Boolean
    ): SupportAppScreen

}