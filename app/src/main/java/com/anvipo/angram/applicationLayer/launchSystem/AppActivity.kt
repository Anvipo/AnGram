package com.anvipo.angram.applicationLayer.launchSystem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.anvipo.angram.R
import com.anvipo.angram.applicationLayer.navigation.coordinator.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorFactoryImp
import com.anvipo.angram.applicationLayer.navigation.router.RouterImp
import com.anvipo.angram.global.ActivityFinishError
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseFragment
import com.anvipo.angram.presentationLayer.common.interfaces.Coordinatorable
import com.anvipo.angram.presentationLayer.common.interfaces.NavigationController
import com.anvipo.angram.presentationLayer.common.interfaces.Presentable
import java.lang.ref.WeakReference

class AppActivity : AppCompatActivity(), NavigationController {

    override val topViewController: Presentable?
        get() {
            if (supportFragmentManager.fragments.isEmpty()) {
                return null
            }

            val topViewController = supportFragmentManager.fragments.last()

            return topViewController as Presentable
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        changeThemeFromSplashToApp()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_container)

        applicationCoordinator.start()
    }


    override fun onBackPressed() {
        currentFragment?.onBackPressed?.invoke() ?: finish()
    }


    override fun push(
        viewController: Presentable,
        hideTabBar: Boolean,
        completion: (() -> Unit)?
    ) {
        val transaction = supportFragmentManager.beginTransaction()

        transaction
            .add(R.id.container, viewController as Fragment)
            .addToBackStack(viewController.tag)
            .commit()

        completion?.invoke()
    }

    override fun setRootViewController(
        viewController: Presentable,
        completion: (() -> Unit)?
    ) {
        repeat(supportFragmentManager.fragments.size) {
            supportFragmentManager.popBackStack()
        }

        val transaction = supportFragmentManager.beginTransaction()

        transaction
            .replace(R.id.container, viewController as Fragment)
            .runOnCommit { completion?.invoke() }
            .commitNow()
    }

    @Throws(ActivityFinishError::class)
    override fun popViewController(): Presentable? {
        if (supportFragmentManager.fragments.isEmpty()) {
            return null
        }

        if (supportFragmentManager.fragments.size == 1) {
            finish()
            throw ActivityFinishError()
        }

        val poppedViewController = supportFragmentManager.fragments.last()

        supportFragmentManager.popBackStackImmediate()

        if (poppedViewController == null) {
            return null
        }

        return poppedViewController as Presentable
    }

    override var isNavigationBarHidden: Boolean = false


    /// PRIVATE

    private val currentFragment: BaseFragment?
        get() = supportFragmentManager.findFragmentById(R.id.container) as BaseFragment

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
