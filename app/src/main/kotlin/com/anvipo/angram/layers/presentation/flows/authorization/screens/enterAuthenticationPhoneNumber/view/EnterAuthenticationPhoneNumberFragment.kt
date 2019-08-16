package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.view


import android.view.View
import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.base.classes.BaseFragment
import com.anvipo.angram.layers.core.dialogFragment.ItemsDialogFragment
import com.anvipo.angram.layers.core.dialogFragment.MessageDialogFragment
import com.anvipo.angram.layers.core.hideKeyboard
import com.anvipo.angram.layers.core.hideWithAnimate
import com.anvipo.angram.layers.core.showWithAnimate
import com.anvipo.angram.layers.core.textWatchers.PhoneNumberTextWatcher
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.di.EnterAuthenticationPhoneNumberModule.enterAuthenticationPhoneNumberPresenterQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.presenter.EnterAuthenticationPhoneNumberViewModel
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.presenter.EnterAuthenticationPhoneNumberViewModelImpl
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import org.koin.android.ext.android.get

class EnterAuthenticationPhoneNumberFragment :
    BaseFragment(),
    EnterAuthenticationPhoneNumberView,
    MessageDialogFragment.OnClickListener,
    ItemsDialogFragment.OnClickListener {

    companion object {
        fun createNewInstance(): EnterAuthenticationPhoneNumberView = EnterAuthenticationPhoneNumberFragment()
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

    override val viewModel: EnterAuthenticationPhoneNumberViewModel by lazy { mPresenter }
    override val layoutRes: Int by lazy { R.layout.fragment_enter_phone_number }

    override val actionBarTitle: String by lazy { getString(R.string.enter_your_phone_number_title) }
    override val actionBar: Toolbar
        get() = enter_phone_number_toolbar as Toolbar

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

    @ProvidePresenter
    fun providePresenter(): EnterAuthenticationPhoneNumberViewModelImpl =
        get(enterAuthenticationPhoneNumberPresenterQualifier)

    @InjectPresenter
    lateinit var mPresenter: EnterAuthenticationPhoneNumberViewModelImpl

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
