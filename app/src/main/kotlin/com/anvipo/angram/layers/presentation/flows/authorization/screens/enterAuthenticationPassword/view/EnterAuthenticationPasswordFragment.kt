package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.view

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.hideWithAnimate
import com.anvipo.angram.layers.core.showWithAnimate
import com.anvipo.angram.layers.core.textWatchers.TextWatcherImpl
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.authorization.screens.base.view.BaseAuthorizationFlowFragment
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.viewModel.EnterAuthenticationPasswordViewModel
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.viewModel.EnterAuthenticationPasswordViewModelFactory
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.viewModel.EnterAuthenticationPasswordViewModelImpl
import kotlinx.android.synthetic.main.fragment_enter_authentication_password.*

class EnterAuthenticationPasswordFragment : BaseAuthorizationFlowFragment() {

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

    override fun setupListeners() {
        enter_password_next_button.setOnClickListener {
            viewModel.onNextButtonPressed(enter_auth_password_edit_text.text.toString())
        }

        enter_auth_password_edit_text.addTextChangedListener(authPasswordTextWatcher)
    }

    override fun showNextButton() {
        enter_password_next_button.showWithAnimate()
    }

    override fun hideNextButton() {
        enter_password_next_button.hideWithAnimate()
    }

    override fun enableNextButton() {
        enter_password_next_button.isEnabled = true
    }

    override fun disableNextButton() {
        enter_password_next_button.isEnabled = false
    }

    private val authPasswordTextWatcher by lazy {
        TextWatcherImpl(
            onEnteredText = { viewModel.onAuthenticationPasswordTextChanged(it) }
        )
    }

}
