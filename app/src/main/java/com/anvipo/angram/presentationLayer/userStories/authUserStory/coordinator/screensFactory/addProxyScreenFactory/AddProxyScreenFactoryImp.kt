package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.addProxyScreenFactory

import androidx.fragment.app.Fragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.addProxy.types.ProxyType
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.addProxy.view.AddProxyFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object AddProxyScreenFactoryImp : AddProxyScreenFactory {

    class AddProxyScreen(
        private val proxyType: ProxyType,
        private val shouldShowBackButton: Boolean
    ) : SupportAppScreen() {
        override fun getFragment(): Fragment =
            AddProxyFragment.createNewInstance(
                proxyType = proxyType,
                shouldShowBackButton = shouldShowBackButton
            ) as Fragment
    }

    override fun createAddProxyScreen(
        proxyType: ProxyType,
        shouldShowBackButton: Boolean
    ): SupportAppScreen = AddProxyScreen(
        proxyType = proxyType,
        shouldShowBackButton = shouldShowBackButton
    )

}