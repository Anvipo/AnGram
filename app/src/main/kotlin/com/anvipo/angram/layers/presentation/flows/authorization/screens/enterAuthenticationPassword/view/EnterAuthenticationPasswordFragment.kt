package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.view

import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.base.classes.BaseFragment
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.di.EnterAuthenticationPasswordModule.enterAuthenticationPasswordPresenterQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.presenter.EnterAuthenticationPasswordPresenter
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.presenter.EnterAuthenticationPasswordPresenterImpl
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_enter_authentication_password.*
import org.koin.android.ext.android.get

class EnterAuthenticationPasswordFragment : BaseFragment(), EnterAuthenticationPasswordView {

    companion object {
        fun createNewInstance(): EnterAuthenticationPasswordView = EnterAuthenticationPasswordFragment()
    }

    override fun setupClickListeners() {
        enter_password_next_button.setOnClickListener {
            presenter.onNextButtonPressed(enter_password_edit_text.text.toString())
        }
    }

    override val presenter: EnterAuthenticationPasswordPresenter by lazy { mPresenter }
    override val actionBarTitle: String by lazy { getString(R.string.enter_password) }
    override val actionBar: Toolbar
        get() = enter_phone_number_toolbar as Toolbar
    override val layoutRes: Int = R.layout.fragment_enter_authentication_password


    @ProvidePresenter
    fun providePresenter(): EnterAuthenticationPasswordPresenterImpl =
        get(enterAuthenticationPasswordPresenterQualifier)

    @InjectPresenter
    lateinit var mPresenter: EnterAuthenticationPasswordPresenterImpl

}
