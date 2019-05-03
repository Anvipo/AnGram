package com.anvipo.angram.presentationLayer.common.interfaces

interface NavigationController {

    fun push(
        viewController: Presentable,
        hideTabBar: Boolean = false,
        completion: (() -> Unit)? = null
    )

    fun setRootViewController(
        viewController: Presentable,
        completion: (() -> Unit)? = null
    )

    fun popViewController(): Presentable?

    val topViewController: Presentable?
    var isNavigationBarHidden: Boolean

}