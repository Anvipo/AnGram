package com.anvipo.angram.layers.application.launchSystem.appActivity.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.observe
import com.anvipo.angram.R
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.navigatorHolderQualifier
import com.anvipo.angram.layers.application.launchSystem.appActivity.di.AppActivityModule.appViewModelFactoryQualifier
import com.anvipo.angram.layers.application.launchSystem.appActivity.types.SetNavigatorEventParameters.REMOVE
import com.anvipo.angram.layers.application.launchSystem.appActivity.types.SetNavigatorEventParameters.SET
import com.anvipo.angram.layers.application.launchSystem.appActivity.viewModel.AppViewModel
import com.anvipo.angram.layers.application.launchSystem.appActivity.viewModel.AppViewModelImpl
import com.anvipo.angram.layers.core.base.classes.BaseActivity
import kotlinx.android.synthetic.main.layout_container.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.*

class AppActivity : BaseActivity() {

    override val viewModel: AppViewModel by viewModels<AppViewModelImpl> {
        get(appViewModelFactoryQualifier)
    }

    override val layoutRes: Int = R.layout.layout_container

    override val rootView: View by lazy { container }

    override fun onCreate(savedInstanceState: Bundle?) {
        changeThemeFromSplashToApp()
        super.onCreate(savedInstanceState)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        viewModel.onResumeFragments()
    }

    override fun onBackPressed() {
        currentFragment.onBackPressed()
    }

    override fun setupViewModelsObservers() {
        super.setupViewModelsObservers()
        viewModel
            .setNavigatorEvents
            .observe(this) {
                @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
                when (it) {
                    SET -> setNavigator()
                    REMOVE -> removeNavigator()
                }
            }
    }


    private val navigatorHolder: NavigatorHolder by inject(navigatorHolderQualifier)

    private val navigator: Navigator by lazy {
        object : SupportAppNavigator(this, supportFragmentManager, R.id.container) {

            override fun setupFragmentTransaction(
                command: Command,
                currentFragment: Fragment?,
                nextFragment: Fragment,
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

    private fun setNavigator() {
        navigatorHolder.setNavigator(navigator)
    }

    private fun removeNavigator() {
        navigatorHolder.removeNavigator()
    }

    private fun changeThemeFromSplashToApp() {
        setTheme(R.style.AppTheme)
    }

}
