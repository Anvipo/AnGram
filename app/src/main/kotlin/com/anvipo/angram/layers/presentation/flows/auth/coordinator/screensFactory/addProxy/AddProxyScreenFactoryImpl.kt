package com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.addProxy

import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.di.AddProxyModule
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.di.AddProxyModule.addProxyScreenQualifier
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.types.ProxyType
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.terrakok.cicerone.android.support.SupportAppScreen

class AddProxyScreenFactoryImpl(
    private val koinScope: Scope
) : AddProxyScreenFactory {

    override fun createAddProxyScreen(
        proxyType: ProxyType,
        shouldShowBackButton: Boolean
    ): SupportAppScreen =
        koinScope.get(addProxyScreenQualifier) {
            parametersOf(
                AddProxyModule.AddProxyScreenParameters(
                    shouldShowBackButton = shouldShowBackButton,
                    proxyType = proxyType
                )
            )
        }

}