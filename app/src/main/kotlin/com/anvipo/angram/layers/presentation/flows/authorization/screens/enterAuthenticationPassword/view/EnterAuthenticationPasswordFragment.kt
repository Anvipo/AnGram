package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.view

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.base.classes.BaseFragment
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.viewModel.EnterAuthenticationPasswordViewModel
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.viewModel.EnterAuthenticationPasswordViewModelFactory
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.viewModel.EnterAuthenticationPasswordViewModelImpl
import kotlinx.android.synthetic.main.fragment_enter_authentication_password.*
import org.koin.android.ext.android.get

class EnterAuthenticationPasswordFragment : BaseFragment() {

    companion object {
        fun createNewInstance(): EnterAuthenticationPasswordFragment =
            EnterAuthenticationPasswordFragment()
    }

    override val viewModel: EnterAuthenticationPasswordViewModel
            by viewModels<EnterAuthenticationPasswordViewModelImpl> {
                authorizationCoordinatorScope!!.get<EnterAuthenticationPasswordViewModelFactory>()
            }
    override val actionBarTitle: String by lazy { getString(R.string.enter_password) }
    override val actionBar: Toolbar
        get() = enter_phone_number_toolbar as Toolbar
    override val layoutRes: Int = R.layout.fragment_enter_authentication_password

    override fun setupClickListeners() {
        enter_password_next_button.setOnClickListener {
            viewModel.onNextButtonPressed(enter_password_edit_text.text.toString())
        }
    }

}
