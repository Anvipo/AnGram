package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.addProxyScreenFactory

import androidx.fragment.app.Fragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.addProxy.types.ProxyType
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.addProxy.view.AddProxyFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object AddProxyScreenFactoryImp : AddProxyScreenFactory {

    class AddProxyScreen(
        private val proxyType: ProxyType
    ) : SupportAppScreen() {
        override fun getFragment(): Fragment = AddProxyFragment.createNewInstance(proxyType) as Fragment
    }

    override fun createAddProxyScreen(proxyType: ProxyType): SupportAppScreen = AddProxyScreen(proxyType)

}