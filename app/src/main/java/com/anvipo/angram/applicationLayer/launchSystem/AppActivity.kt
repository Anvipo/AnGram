package com.anvipo.angram.applicationLayer.launchSystem

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import com.anvipo.angram.R
import com.anvipo.angram.applicationLayer.navigation.coordinator.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorsFactory
import com.anvipo.angram.applicationLayer.navigation.coordinator.coordinatorFactory.ApplicationCoordinatorsFactoryImp
import com.anvipo.angram.applicationLayer.navigation.router.Routable
import com.anvipo.angram.applicationLayer.navigation.router.RouterImp
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGatewayImp
import com.anvipo.angram.global.ActivityFinishError
import com.anvipo.angram.global.assertionFailure
import com.anvipo.angram.global.debugLog
import com.anvipo.angram.global.showDebugToast
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseFragment
import com.anvipo.angram.presentationLayer.common.interfaces.Coordinatorable
import com.anvipo.angram.presentationLayer.common.interfaces.NavigationController
import com.anvipo.angram.presentationLayer.common.interfaces.Presentable
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import java.lang.ref.WeakReference

class AppActivity :
    AppCompatActivity(),
    NavigationController {

    override val thisContext: Context
        get() = this

    override var onBackPressed: (() -> Unit)? = {
        this.onBackPressed()
    }

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
        currentFragment?.onBackPressed?.invoke() ?: super.onBackPressed()
    }


    /// NAVIGATION CONTROLLER


    override fun push(
        viewController: Presentable,
        hideTabBar: Boolean,
        completion: (() -> Unit)?
    ) {
        supportFragmentManager.transaction {
            replace(R.id.container, viewController as Fragment)
            addToBackStack(null)
        }

        completion?.invoke()
    }

    override fun setRootViewController(
        viewController: Presentable,
        completion: (() -> Unit)?
    ) {
        repeat(supportFragmentManager.fragments.size) {
            supportFragmentManager.popBackStackImmediate()
        }

        supportFragmentManager.transaction(now = true) {
            replace(R.id.container, viewController as Fragment)
            runOnCommit { completion?.invoke() }
        }
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

    private fun changeThemeFromSplashToApp() {
        setTheme(R.style.AppTheme)
    }


    private val currentFragment: BaseFragment?
        get() = supportFragmentManager.findFragmentById(R.id.container) as BaseFragment

    // TODO: apply DI
    private val applicationCoordinator: Coordinatorable by lazy {
        val coordinatorsFactory: ApplicationCoordinatorsFactory = ApplicationCoordinatorsFactoryImp()
        val rootController: WeakReference<NavigationController> = WeakReference(this)
        val router: Routable = RouterImp(rootController = rootController)
        val tgClient: Client = Client.create(updatesHandler, updatesExceptionHandler, defaultExceptionHandler)
        val tdLibGateway: TDLibGateway =
            TDLibGatewayImp(tgClient = tgClient)

        ApplicationCoordinator(
            coordinatorsFactory = coordinatorsFactory,
            router = router,
            tdLibGateway = tdLibGateway
        )
    }

    private val updatesHandler = Client.ResultHandler { tdApiObject ->
        val tag = "${this::class.java.simpleName} updatesHandler"

        when (tdApiObject) {
            is TdApi.UpdateOption -> onUpdateOption(tag, tdApiObject)
            is TdApi.UpdateAuthorizationState -> onUpdateAuthorizationState(tag, tdApiObject)
            else -> {
                val message = tdApiObject.toString()

                debugLog(message)
                showDebugToast(message)
            }
        }
    }

    private val updatesExceptionHandler = Client.ExceptionHandler { error ->
        val message = error.localizedMessage

        debugLog(message)
        showDebugToast(message)
    }

    private val defaultExceptionHandler = Client.ExceptionHandler { error ->
        val message = error.localizedMessage

        debugLog(message)
        showDebugToast(message)
    }


    ///


    private fun onUpdateAuthorizationState(
        tag: String,
        updateAuthorizationState: TdApi.UpdateAuthorizationState
    ) {
        when (val authorizationState = updateAuthorizationState.authorizationState) {
            is TdApi.AuthorizationStateWaitTdlibParameters -> {
                val message = "$tag: TDLib waits parameters"

                debugLog(message)
                showDebugToast(message)
            }
            is TdApi.AuthorizationStateWaitEncryptionKey -> {
                val message = "$tag: TDLib waits encryption key (isEncrypted: ${authorizationState.isEncrypted})"

                debugLog(message)
                showDebugToast(message)
            }
            else -> {
                val message = "$tag: $authorizationState"

                debugLog(message)
                showDebugToast(message)
            }
        }
    }

    private fun onUpdateOption(
        tag: String,
        updateOption: TdApi.UpdateOption
    ) {
        val updateOptionName = updateOption.name

        val updateOptionValue: Any =
            when (val unrecognizedUpdateOptionValue = updateOption.value) {
                is TdApi.OptionValueString -> {
                    unrecognizedUpdateOptionValue.value
                }
                else -> {
                    val message = "$tag: Unspecified TdApi.UpdateOption"

                    debugLog(message)
                    showDebugToast(message)
                    assertionFailure()
                    ""
                }
            }

        val message = "$tag: $updateOptionName: $updateOptionValue"
        debugLog(message)
        showDebugToast(message)
    }

}
