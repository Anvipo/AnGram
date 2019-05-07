package com.anvipo.angram.applicationLayer.launchSystem

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.anvipo.angram.R
import com.anvipo.angram.applicationLayer.di.LaunchSystemModule
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule
import com.anvipo.angram.applicationLayer.navigation.coordinator.ApplicationCoordinator
import com.anvipo.angram.applicationLayer.types.SystemMessageReceiveChannel
import com.anvipo.angram.coreLayer.MessageDialogFragment
import com.anvipo.angram.coreLayer.debugLog
import com.anvipo.angram.coreLayer.message.SystemMessage
import com.anvipo.angram.coreLayer.message.SystemMessageType
import com.anvipo.angram.coreLayer.mvp.MvpAppCompatActivity
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseFragment
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import kotlin.coroutines.CoroutineContext

class AppActivity : MvpAppCompatActivity(), CoroutineScope {

    // TODO: add error handler
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        changeThemeFromSplashToApp()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_container)

        job = Job()

        if (savedInstanceState == null) {
            applicationCoordinator.coldStart()
        } else {
            debugLog("$simpleName onCreate savedInstanceState != null")
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        subscribeOnSystemMessages()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        unsubscribeOnSystemMessages()
        super.onPause()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onBackPressed() {
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }


    /// PRIVATE


    private fun changeThemeFromSplashToApp() {
        setTheme(R.style.AppTheme)
    }

    private val currentFragment: BaseFragment?
        get() = supportFragmentManager.findFragmentById(R.id.container) as? BaseFragment

    private val applicationCoordinator: ApplicationCoordinator by inject(LaunchSystemModule.applicationCoordinator)
    private val navigatorHolder: NavigatorHolder by inject(SystemInfrastructureModule.navigatorHolder)

    private val navigator: Navigator by lazy {
        object : SupportAppNavigator(this, supportFragmentManager, R.id.container) {

            override fun setupFragmentTransaction(
                command: Command?,
                currentFragment: Fragment?,
                nextFragment: Fragment?,
                fragmentTransaction: FragmentTransaction
            ) {
                fragmentTransaction.setReorderingAllowed(true)
            }

        }
    }

    private lateinit var job: Job

    private val systemMessageReceiveChannel: SystemMessageReceiveChannel
            by inject(LaunchSystemModule.systemMessageReceiveChannel)

    private fun subscribeOnSystemMessages() {
        val onReceivedMessage: (SystemMessage) -> Unit = { (text, type, shouldBeShownToUser, shouldBeShownInLogs) ->
            if (shouldBeShownToUser) {
                when (type) {
                    SystemMessageType.TOAST -> showToastMessage(text)
                    SystemMessageType.ALERT -> showAlertMessage(text)
                }
            }

            if (shouldBeShownInLogs) {
                Log.d(App.TAG, text)
            }
        }

        launch {
            val receivedMessage = systemMessageReceiveChannel.receive()

            onReceivedMessage(receivedMessage)
        }
    }

    private fun unsubscribeOnSystemMessages() {
        systemMessageReceiveChannel.cancel(CancellationException("AppActivity onPause"))
    }

    private fun showAlertMessage(message: String) {
        MessageDialogFragment
            .create(message = message)
            .show(supportFragmentManager, null)
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this.applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private val simpleName: String
        get() = this::class.java.simpleName

}
