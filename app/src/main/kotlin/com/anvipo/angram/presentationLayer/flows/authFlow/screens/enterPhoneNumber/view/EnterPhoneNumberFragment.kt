package com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterPhoneNumber.view


import android.view.View
import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.R
import com.anvipo.angram.coreLayer.base.baseClasses.BaseFragment
import com.anvipo.angram.coreLayer.dialogFragment.ItemsDialogFragment
import com.anvipo.angram.coreLayer.dialogFragment.MessageDialogFragment
import com.anvipo.angram.coreLayer.hideKeyboard
import com.anvipo.angram.coreLayer.hideWithAnimate
import com.anvipo.angram.coreLayer.showWithAnimate
import com.anvipo.angram.presentationLayer.common.PhoneNumberTextWatcher
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterPhoneNumber.di.EnterPhoneNumberModule.enterPhoneNumberPresenterQualifier
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenter
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenterImp
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import org.koin.android.ext.android.get

class EnterPhoneNumberFragment :
    BaseFragment(),
    EnterPhoneNumberView,
    MessageDialogFragment.OnClickListener,
    ItemsDialogFragment.OnClickListener {

    companion object {
        @JvmStatic
        fun createNewInstance(): EnterPhoneNumberView = EnterPhoneNumberFragment()
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

    override val presenter: EnterPhoneNumberPresenter by lazy { mPresenter }
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
        presenter.onItemClicked(index)
    }

    @ProvidePresenter
    fun providePresenter(): EnterPhoneNumberPresenterImp =
        get(enterPhoneNumberPresenterQualifier)

    @InjectPresenter
    lateinit var mPresenter: EnterPhoneNumberPresenterImp

    private val phoneNumberTextWatcher by lazy {
        PhoneNumberTextWatcher(
            onEnteredCleanedPhoneNumber = { presenter.onPhoneNumberTextChanged(it) }
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
        presenter.onAddProxyButtonPressed()
    }

    private fun onClickedPhoneNumberNextButton(
        @Suppress("UNUSED_PARAMETER") view: View
    ) {
        val enteredPhoneNumber = enter_phone_number_edit_text.text.toString()

        presenter.onNextButtonPressed(enteredPhoneNumber)
    }

}
