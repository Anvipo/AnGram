package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.addProxyScreenFactory

import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.addProxy.types.ProxyType
import ru.terrakok.cicerone.android.support.SupportAppScreen

interface AddProxyScreenFactory {

    fun createAddProxyScreen(
        proxyType: ProxyType,
        shouldShowBackButton: Boolean
    ): SupportAppScreen

}