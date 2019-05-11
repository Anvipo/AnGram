package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationPassword.view

import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.R
import com.anvipo.angram.coreLayer.base.baseClasses.BaseFragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationPassword.di.EnterAuthenticationPasswordModule
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationPassword.presenter.EnterAuthenticationPasswordPresenter
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationPassword.presenter.EnterAuthenticationPasswordPresenterImp
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_enter_authentication_password.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.android.ext.android.get

class EnterAuthenticationPasswordFragment : BaseFragment(), EnterAuthenticationPasswordView {

    companion object {
        @JvmStatic
        fun createNewInstance(): EnterAuthenticationPasswordView = EnterAuthenticationPasswordFragment()
    }

    override fun setupClickListeners() {
        enter_password_next_button.setOnClickListener {
            presenter.onNextButtonPressed(enter_password_edit_text.text.toString())
        }
    }

    override val presenter: EnterAuthenticationPasswordPresenter by lazy { mPresenter }
    override val actionBarTitle: String by lazy { getString(R.string.enter_password) }
    override val actionBar: Toolbar by lazy { toolbar }
    override val layoutRes: Int = R.layout.fragment_enter_authentication_password


    @Suppress("ProtectedInFinal")
    @ProvidePresenter
    protected fun providePresenter(): EnterAuthenticationPasswordPresenterImp =
        get(EnterAuthenticationPasswordModule.enterAuthenticationPasswordPresenter)

    @InjectPresenter
    internal lateinit var mPresenter: EnterAuthenticationPasswordPresenterImp

}
