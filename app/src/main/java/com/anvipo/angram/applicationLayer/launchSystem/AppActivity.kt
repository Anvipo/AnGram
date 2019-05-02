package com.anvipo.angram.applicationLayer.launchSystem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.anvipo.angram.R
import com.anvipo.angram.applicationLayer.navigation.coordinator.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorFactoryImp
import com.anvipo.angram.applicationLayer.navigation.router.RouterImp
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseFragment
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


    override fun onBackPressed() {
        currentFragment?.onBackPressed?.invoke() ?: super.onBackPressed()
    }


    override fun push(screen: Presentable, animated: Boolean, completion: (() -> Unit)?) {
        TODO("not implemented")
    }

    override fun set(rootScreen: Presentable, animated: Boolean, completion: (() -> Unit)?) {
        repeat(supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }

        val transaction = supportFragmentManager.beginTransaction()

        transaction
            .replace(R.id.container, rootScreen as Fragment)
            .runOnCommit { completion?.invoke() }
            .commitNow()
    }

    override var isNavigationBarHidden: Boolean = false


    /// PRIVATE

    private val currentFragment: BaseFragment?
        get() = supportFragmentManager.findFragmentById(R.id.container) as? BaseFragment

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
