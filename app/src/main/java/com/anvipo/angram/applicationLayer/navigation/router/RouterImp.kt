package com.anvipo.angram.applicationLayer.navigation.router

import com.anvipo.angram.presentationLayer.common.interfaces.NavigationController
import com.anvipo.angram.presentationLayer.common.interfaces.Presentable
import java.lang.ref.WeakReference

class RouterImp(
    private val rootController: WeakReference<NavigationController>
) : Routable {

    override var presentedScreen: WeakReference<Presentable>? = null

    override fun present(
        screen: Presentable,
        animated: Boolean,
        withNavBar: Boolean
    ) {
        TODO("not implemented")
    }

    override fun push(
        screen: Presentable,
        animated: Boolean,
        hideTabBar: Boolean,
        completion: (() -> Unit)?
    ) {
        TODO("not implemented")
    }

    override fun popScreen(animated: Boolean) {
        TODO("not implemented")
    }

    override fun popToScreen(
        screen: Presentable,
        animated: Boolean
    ) {
        TODO("not implemented")
    }

    override fun dismissScreen(
        animated: Boolean,
        completion: (() -> Unit)?
    ) {
        TODO("not implemented")
    }

    override fun set(
        rootScreen: Presentable,
        animated: Boolean,
        hideBar: Boolean
    ) {
        rootController.get()?.isNavigationBarHidden = hideBar

        rootController.get()?.set(
            rootScreen = rootScreen,
            animated = animated
        ) {
            presentedScreen = WeakReference(rootScreen)
        }
    }

    override fun popToRootScreen(animated: Boolean) {
        TODO("not implemented")
    }

    override fun pushAsPresent(module: Presentable) {
        TODO("not implemented")
    }

    override fun popAsPresent() {
        TODO("not implemented")
    }


    /// PRIVATE


    private val completions: MutableMap<WeakReference<Presentable>, () -> Unit> = mutableMapOf()


}