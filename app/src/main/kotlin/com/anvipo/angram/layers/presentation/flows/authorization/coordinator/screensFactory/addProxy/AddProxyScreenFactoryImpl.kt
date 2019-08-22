package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.addProxy

import com.anvipo.angram.layers.presentation.screens.addProxy.types.ProxyType
import com.anvipo.angram.layers.presentation.screens.addProxy.view.navigation.AddProxyScreen
import com.anvipo.angram.layers.presentation.screens.addProxy.view.navigation.AddProxyScreenParameters
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope

class AddProxyScreenFactoryImpl(
    private val koinScope: Scope
) : AddProxyScreenFactory {

    override fun createAddProxyScreen(
        proxyType: ProxyType,
        shouldShowBackButton: Boolean
    ): AddProxyScreen =
        koinScope.get {
            parametersOf(
                AddProxyScreenParameters(
                    shouldShowBackButton = shouldShowBackButton,
                    proxyType = proxyType
                )
            )
        }

}