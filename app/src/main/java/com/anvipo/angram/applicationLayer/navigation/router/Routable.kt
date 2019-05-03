package com.anvipo.angram.applicationLayer.navigation.router

import com.anvipo.angram.presentationLayer.common.interfaces.Presentable
import java.lang.ref.WeakReference

interface Routable {

    val presentedViewController: WeakReference<Presentable>?

    fun present(
        viewController: Presentable,
        withNavBar: Boolean = false,
        completion: (() -> Unit)? = null
    )

    fun push(
        viewController: Presentable,
        hideTabBar: Boolean = false,
        completion: (() -> Unit)? = null
    )

    fun popViewController()

    fun popToViewController(viewController: Presentable)

    fun dismiss(completion: (() -> Unit)? = null)

    fun setRootViewController(
        viewController: Presentable,
        hideBar: Boolean = false
    )

    fun popToRootViewController()

    fun pushAsPresent(viewController: Presentable)

    fun popAsPresent()

}