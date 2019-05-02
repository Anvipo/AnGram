package com.anvipo.angram.applicationLayer.launchSystem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anvipo.angram.R
import com.anvipo.angram.applicationLayer.navigation.coordinator.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorFactoryImp
import com.anvipo.angram.applicationLayer.navigation.router.RouterImp
import com.anvipo.angram.presentationLayer.common.interfaces.Coordinatorable
import com.anvipo.angram.presentationLayer.common.interfaces.NavigationController
import com.anvipo.angram.presentationLayer.common.interfaces.Presentable
import java.lang.ref.WeakReference

class AppActivity : AppCompatActivity(), NavigationController {

    override fun onCreate(savedInstanceState: Bundle?) {
        changeThemeFromSplashToApp()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_container)

        applicationCoordinator.start()
    }


    override fun push(screen: Presentable, animated: Boolean, completion: (() -> Unit)?) {
        TODO("not implemented")
    }

    override fun set(rootScreen: Presentable, animated: Boolean, completion: (() -> Unit)?) {
        TODO("not implemented")
    }

    override var isNavigationBarHidden: Boolean = false


    /// PRIVATE

    // TODO: apply DI
    private val applicationCoordinator: Coordinatorable by lazy {
        ApplicationCoordinator(
            coordinatorFactory = ApplicationCoordinatorFactoryImp(),
            router = RouterImp(rootController = WeakReference(this))
        )
    }


    private fun changeThemeFromSplashToApp() {
        setTheme(R.style.AppTheme)
    }

}
