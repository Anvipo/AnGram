package com.anvipo.angram.applicationLayer.launchSystem.appActivity.view

import android.os.Bundle
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.anvipo.angram.R
import com.anvipo.angram.applicationLayer.di.LaunchSystemModule.appPresenterQualifier
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.navigatorHolderQualifier
import com.anvipo.angram.applicationLayer.launchSystem.appActivity.presenter.AppPresenter
import com.anvipo.angram.applicationLayer.launchSystem.appActivity.presenter.AppPresenterImp
import com.anvipo.angram.coreLayer.base.baseClasses.BaseActivity
import com.anvipo.angram.coreLayer.base.baseClasses.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.layout_container.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.*

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


    override fun setupClickListeners(): Unit = Unit

    override val presenter: AppPresenter by lazy { mPresenter }

    override val layoutRes: Int = R.layout.layout_container

    override val rootView: View by lazy { container }


    /// PRIVATE


    private fun changeThemeFromSplashToApp() {
        setTheme(R.style.AppTheme)
    }

    private val currentFragment: BaseFragment?
        get() = supportFragmentManager.findFragmentById(R.id.container) as? BaseFragment

    private val navigatorHolder: NavigatorHolder by inject(navigatorHolderQualifier)

    private val navigator: Navigator by lazy {
        object : SupportAppNavigator(this, supportFragmentManager, R.id.container) {

            override fun setupFragmentTransaction(
                command: Command?,
                currentFragment: Fragment?,
                nextFragment: Fragment?,
                fragmentTransaction: FragmentTransaction
            ) {
                @AnimatorRes @AnimRes val enter: Int
                @AnimatorRes @AnimRes val exit: Int
                @AnimatorRes @AnimRes val popEnter: Int
                @AnimatorRes @AnimRes val popExit: Int
                when (command) {
                    is Back -> {
                        enter = R.anim.enter_from_left
                        popExit = R.anim.exit_to_left

                        popEnter = R.anim.enter_from_right
                        exit = R.anim.exit_to_right
                    }
                    is BackTo -> {
                        enter = R.anim.enter_from_left
                        popExit = R.anim.exit_to_left

                        popEnter = R.anim.enter_from_right
                        exit = R.anim.exit_to_right
                    }
                    is Forward -> {
                        enter = R.anim.enter_from_right
                        popExit = R.anim.exit_to_right

                        popEnter = R.anim.enter_from_left
                        exit = R.anim.exit_to_left
                    }
                    is Replace -> {
                        enter = android.R.anim.fade_in
                        popExit = android.R.anim.fade_out

                        popEnter = android.R.anim.fade_in
                        exit = android.R.anim.fade_out
                    }
                    else -> {
                        enter = R.anim.enter_from_right
                        popExit = R.anim.exit_to_right

                        popEnter = R.anim.enter_from_left
                        exit = R.anim.exit_to_left
                    }
                }

                fragmentTransaction.setCustomAnimations(
                    enter,
                    exit,
                    popEnter,
                    popExit
                )

                fragmentTransaction.setReorderingAllowed(true)
            }

        }
    }

    @Suppress("ProtectedInFinal")
    @ProvidePresenter
    protected fun providePresenter(): AppPresenterImp =
        get(appPresenterQualifier)

    @InjectPresenter
    internal lateinit var mPresenter: AppPresenterImp

}
