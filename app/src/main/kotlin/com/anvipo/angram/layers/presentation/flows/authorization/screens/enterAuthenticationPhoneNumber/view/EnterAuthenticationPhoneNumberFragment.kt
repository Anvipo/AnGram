package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.dialogFragment.ItemsDialogFragment
import com.anvipo.angram.layers.core.dialogFragment.MessageDialogFragment
import com.anvipo.angram.layers.core.hideKeyboard
import com.anvipo.angram.layers.core.hideWithAnimate
import com.anvipo.angram.layers.core.showWithAnimate
import com.anvipo.angram.layers.core.textWatchers.PhoneNumberTextWatcher
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
<<<<<<< develop
import com.anvipo.angram.layers.presentation.flows.authorization.screens.base.view.BaseAuthorizationFlowFragment
=======
>>>>>>> Change DI order loading modules
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.viewModel.EnterAuthenticationPhoneNumberViewModel
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.viewModel.EnterAuthenticationPhoneNumberViewModelFactory
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.viewModel.EnterAuthenticationPhoneNumberViewModelImpl
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*

class EnterAuthenticationPhoneNumberFragment :
    BaseAuthorizationFlowFragment(),
    MessageDialogFragment.OnClickListener,
    ItemsDialogFragment.OnClickListener {

    companion object {
        fun createNewInstance(): EnterAuthenticationPhoneNumberFragment =
            EnterAuthenticationPhoneNumberFragment()

        const val ENTERED_PHONE_NUMBER: String = "entered_phone_number"
    }

    override val viewModel: EnterAuthenticationPhoneNumberViewModel
            by viewModels<EnterAuthenticationPhoneNumberViewModelImpl> {
                authorizationCoordinatorScope!!.get<EnterAuthenticationPhoneNumberViewModelFactory>()
            }

    override val layoutRes: Int by lazy { R.layout.fragment_enter_phone_number }

    override val actionBarTitle: String by lazy { getString(R.string.enter_your_phone_number_title) }
    override val actionBar: Toolbar
        get() = enter_phone_number_toolbar as Toolbar

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(ENTERED_PHONE_NUMBER, enter_phone_number_edit_text?.text?.toString())
    }

    override fun setupClickListeners() {
        enter_phone_number_add_proxy_button.setOnClickListener(::onClickedPhoneNumberAddProxyButton)

        enter_phone_number_next_button.setOnClickListener(::onClickedPhoneNumberNextButton)

        enter_phone_number_edit_text.setOnFocusChangeListener(::onChangedFocusInPhoneNumberEditText)

        enter_phone_number_edit_text.addTextChangedListener(phoneNumberTextWatcher)
    }

    override fun setupUI() {
        enter_phone_number_edit_text.setText("+")
    }

    override fun itemClicked(
        tag: String,
        index: Int
    ) {
        viewModel.onItemClicked(index)
    }

    override fun setupViewModelsObservers() {
        super.setupViewModelsObservers()
        viewModel
            .enterAuthenticationPhoneNumberScreenSavedInputDataEvents
            .observe(this) {
                enter_phone_number_edit_text.setText(it.authenticationPhoneNumber)
            }
    }

    override fun enableNextButton() {
        enter_phone_number_next_button.isEnabled = true
    }

    override fun disableNextButton() {
        enter_phone_number_next_button.isEnabled = false
    }

    override fun showNextButton() {
        enter_phone_number_next_button.showWithAnimate()
    }

    override fun hideNextButton() {
        enter_phone_number_next_button.hideWithAnimate()
    }


    private val phoneNumberTextWatcher by lazy {
        PhoneNumberTextWatcher(
            onEnteredCleanedPhoneNumber = { viewModel.onPhoneNumberTextChanged(it) }
        )
    }

    private fun onChangedFocusInPhoneNumberEditText(
        view: View,
        hasFocus: Boolean
    ) {
        val lostFocus = !hasFocus

        if (lostFocus) {
            context?.hideKeyboard(view)
        }
    }

    private fun onClickedPhoneNumberAddProxyButton(
        @Suppress("UNUSED_PARAMETER") view: View
    ) {
        viewModel.onAddProxyButtonPressed()
    }

    private fun onClickedPhoneNumberNextButton(
        @Suppress("UNUSED_PARAMETER") view: View
    ) {
        val enteredPhoneNumber = enter_phone_number_edit_text.text.toString()

        viewModel.onNextButtonPressed(enteredPhoneNumber)
    }

}
