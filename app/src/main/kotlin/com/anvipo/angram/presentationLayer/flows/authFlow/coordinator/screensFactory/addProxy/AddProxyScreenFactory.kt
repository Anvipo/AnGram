package com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.addProxy

import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.types.ProxyType
import ru.terrakok.cicerone.android.support.SupportAppScreen

interface AddProxyScreenFactory {

    fun createAddProxyScreen(
        proxyType: ProxyType,
        shouldShowBackButton: Boolean
    ): SupportAppScreen

}