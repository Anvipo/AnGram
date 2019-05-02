package com.anvipo.angram.presentationLayer.common.interfaces

interface NavigationController {

    fun push(
        screen: Presentable,
        animated: Boolean = true,
        completion: (() -> Unit)? = null
    )

    fun set(
        rootScreen: Presentable,
        animated: Boolean,
        completion: (() -> Unit)? = null
    )

    var isNavigationBarHidden: Boolean

}