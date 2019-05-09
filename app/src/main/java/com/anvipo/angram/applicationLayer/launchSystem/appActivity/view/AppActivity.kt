package com.anvipo.angram.applicationLayer.launchSystem.appActivity.view

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.anvipo.angram.R
import com.anvipo.angram.applicationLayer.di.LaunchSystemModule
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule
import com.anvipo.angram.applicationLayer.launchSystem.appActivity.presenter.AppPresenter
import com.anvipo.angram.applicationLayer.launchSystem.appActivity.presenter.AppPresenterImp
import com.anvipo.angram.coreLayer.MessageDialogFragment
import com.anvipo.angram.coreLayer.base.baseClasses.BaseActivity
import com.anvipo.angram.coreLayer.base.baseClasses.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command

class AppActivity : BaseActivity(), AppView {

    override fun onCreate(savedInstanceState: Bundle?) {
        changeThemeFromSplashToApp()
        super.onCreate(savedInstanceState)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        presenter.onResumeFragments()
    }

    override fun onPause() {
        presenter.onPause()
        super.onPause()
    }


    override fun setNavigator() {
        navigatorHolder.setNavigator(navigator)
    }

    override fun removeNavigator() {
        navigatorHolder.removeNavigator()
    }


    override fun onBackPressed() {
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }


    override fun showAlertMessage(text: String) {
        MessageDialogFragment
            .create(message = text)
            .show(supportFragmentManager, null)
    }

    override fun showToastMessage(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun setupClickListeners(): Unit = Unit

    override val presenter: AppPresenter by lazy { mPresenter }

    override val layoutRes: Int = R.layout.layout_container


    /// PRIVATE


    private fun changeThemeFromSplashToApp() {
        setTheme(R.style.AppTheme)
    }

    private val currentFragment: BaseFragment?
        get() = supportFragmentManager.findFragmentById(R.id.container) as? BaseFragment

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

    @Suppress("ProtectedInFinal")
    @ProvidePresenter
    protected fun providePresenter(): AppPresenterImp =
        get(LaunchSystemModule.appPresenter)

    @InjectPresenter
    internal lateinit var mPresenter: AppPresenterImp

}
