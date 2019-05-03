package com.anvipo.angram.applicationLayer.launchSystem

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.R
import com.anvipo.angram.applicationLayer.navigation.coordinator.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorsFactoryImp
import com.anvipo.angram.applicationLayer.navigation.router.RouterImp
import com.anvipo.angram.global.ActivityFinishError
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseFragment
import com.anvipo.angram.presentationLayer.common.interfaces.Coordinatorable
import com.anvipo.angram.presentationLayer.common.interfaces.NavigationController
import com.anvipo.angram.presentationLayer.common.interfaces.Presentable
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import java.lang.ref.WeakReference

class AppActivity :
    AppCompatActivity(),
    NavigationController,
    Client.ResultHandler,
    Client.ExceptionHandler {

    override fun onException(e: Throwable?) {
        print("onResult")
    }

    override fun onResult(`object`: TdApi.Object?) {
        print("onResult")
    }

    private lateinit var tgClient: Client

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

        val getAuthStateRequest = TdApi.GetAuthorizationState()

        tgClient = Client.create(this, this, this)

        val getAuthStateResultHandler = Client.ResultHandler { result ->
            if (BuildConfig.DEBUG) {
                Log.d(App.TAG, result.toString())
            }
        }

        val getAuthStateExceptionHandler = Client.ExceptionHandler { exception ->
            Log.e(App.TAG, exception.localizedMessage)
        }

        tgClient.send(getAuthStateRequest, getAuthStateResultHandler, getAuthStateExceptionHandler)
    }


    override fun onBackPressed() {
        currentFragment?.onBackPressed?.invoke() ?: super.onBackPressed()
    }


    /// NAVIGATION CONTROLLER


    override fun push(
        viewController: Presentable,
        hideTabBar: Boolean,
        completion: (() -> Unit)?
    ) {
        val transaction = supportFragmentManager.beginTransaction()

        transaction
            .replace(R.id.container, viewController as Fragment)
            .addToBackStack(null)
            .commit()

        completion?.invoke()
    }

    override fun setRootViewController(
        viewController: Presentable,
        completion: (() -> Unit)?
    ) {
        repeat(supportFragmentManager.fragments.size) {
            supportFragmentManager.popBackStackImmediate()
        }

        val transaction = supportFragmentManager.beginTransaction()

        transaction
            .replace(R.id.container, viewController as Fragment)
            .runOnCommit {
                completion?.invoke()
            }
            .commitNow()
    }

    @Throws(ActivityFinishError::class)
    override fun popViewController(): Presentable? {
        if (supportFragmentManager.fragments.isEmpty()) {
            return null
        }

        if (supportFragmentManager.backStackEntryCount < 1) {
            super.onBackPressed()
            throw ActivityFinishError(message = "supportFragmentManager.backStackEntryCount < 1.")
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
            coordinatorsFactory = ApplicationCoordinatorsFactoryImp(),
            router = RouterImp(rootController = WeakReference(this))
        )
    }


    private fun changeThemeFromSplashToApp() {
        setTheme(R.style.AppTheme)
    }

}
