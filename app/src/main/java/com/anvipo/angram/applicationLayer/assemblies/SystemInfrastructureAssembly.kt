package com.anvipo.angram.applicationLayer.assemblies

import com.anvipo.angram.applicationLayer.launchSystem.AppActivity
import com.anvipo.angram.applicationLayer.navigation.router.Routable
import com.anvipo.angram.applicationLayer.navigation.router.RouterImp
import com.anvipo.angram.presentationLayer.common.interfaces.NavigationController
import java.lang.ref.WeakReference

object SystemInfrastructureAssembly {

    @Suppress("MemberVisibilityCanBePrivate")
    val rootViewController: WeakReference<NavigationController> by lazy { AppActivity.weakInstance }

    val router: Routable by lazy { RouterImp(rootController = rootViewController) }


}