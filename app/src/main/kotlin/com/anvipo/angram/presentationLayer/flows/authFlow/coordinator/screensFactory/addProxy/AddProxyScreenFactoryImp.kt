package com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.addProxy

import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.di.AddProxyModule
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.di.AddProxyModule.addProxyScreenCodeScreenQualifier
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.types.ProxyType
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.terrakok.cicerone.android.support.SupportAppScreen

class AddProxyScreenFactoryImp(
    private val koinScope: Scope
) : AddProxyScreenFactory {

    override fun createAddProxyScreen(
        proxyType: ProxyType,
        shouldShowBackButton: Boolean
    ): SupportAppScreen =
        koinScope.get(addProxyScreenCodeScreenQualifier) {
            parametersOf(
                AddProxyModule.AddProxyScreenParameters(
                    shouldShowBackButton = shouldShowBackButton,
                    proxyType = proxyType
                )
            )
        }

}