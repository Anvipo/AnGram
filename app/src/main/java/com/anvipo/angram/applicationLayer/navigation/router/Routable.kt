package com.anvipo.angram.applicationLayer.navigation.router

import com.anvipo.angram.presentationLayer.common.interfaces.Presentable
import java.lang.ref.WeakReference

interface Routable {

    val presentedScreen: WeakReference<Presentable>?

    fun present(
        screen: Presentable,
        animated: Boolean = true,
        withNavBar: Boolean = false
    )

    fun push(
        screen: Presentable,
        animated: Boolean = true,
        hideTabBar: Boolean = false,
        completion: (() -> Unit)? = null
    )

    fun popScreen(animated: Boolean = true)

    fun popToScreen(
        screen: Presentable,
        animated: Boolean = true
    )

    fun dismissScreen(
        animated: Boolean = true,
        completion: (() -> Unit)? = null
    )

    fun set(
        rootScreen: Presentable,
        animated: Boolean = true,
        hideBar: Boolean = false
    )

    fun popToRootScreen(animated: Boolean = true)

    fun pushAsPresent(module: Presentable)

    fun popAsPresent()

}