package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.view


import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.base.classes.BaseFragment
import com.anvipo.angram.layers.core.hideKeyboard
import com.anvipo.angram.layers.core.hideWithAnimate
import com.anvipo.angram.layers.core.showWithAnimate
import com.anvipo.angram.layers.core.textWatchers.TextWatcherImpl
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule.enterAuthenticationCodePresenterQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.presenter.EnterAuthenticationCodePresenter
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.presenter.EnterAuthenticationCodePresenterImpl
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_enter_authentication_code.*
import org.koin.android.ext.android.get


class EnterAuthenticationCodeFragment : BaseFragment(), EnterAuthenticationCodeView {

    companion object {
        private const val ARG_EXPECTED_CODE_LENGTH = "arg_expected_code_length"
        private const val ARG_ENTERED_PHONE_NUMBER = "arg_entered_phone_number"
        private const val ARG_REGISTRATION_REQUIRED = "arg_registration_required"
        private const val ARG_TERMS_OF_SERVICE_TEXT = "arg_terms_of_service_text"

        fun createNewInstance(
            shouldShowBackButton: Boolean,
            expectedCodeLength: Int,
            enteredPhoneNumber: String,
            registrationRequired: Boolean,
            termsOfServiceText: String
        ): EnterAuthenticationCodeView =
            EnterAuthenticationCodeFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_SHOULD_SHOW_BACK_BUTTON, shouldShowBackButton)
                    putBoolean(ARG_REGISTRATION_REQUIRED, registrationRequired)
                    putInt(ARG_EXPECTED_CODE_LENGTH, expectedCodeLength)
                    putString(ARG_ENTERED_PHONE_NUMBER, enteredPhoneNumber)
                    putString(ARG_TERMS_OF_SERVICE_TEXT, termsOfServiceText)
                }
            }
    }

    @ExperimentalUnsignedTypes
    override fun extractDataFromBundle() {
        val expectedCodeLength = arguments?.getInt(ARG_EXPECTED_CODE_LENGTH) ?: 5
        presenter.onGetExpectedCodeLength(expectedCodeLength.toUInt())

        val enteredPhoneNumber = arguments?.getString(ARG_ENTERED_PHONE_NUMBER) ?: ""
        presenter.onGetEnteredPhoneNumber(enteredPhoneNumber)

        val registrationRequired = arguments?.getBoolean(ARG_REGISTRATION_REQUIRED) ?: false
        presenter.onGetRegistrationRequired(registrationRequired)

        val termsOfServiceText = arguments?.getString(ARG_TERMS_OF_SERVICE_TEXT) ?: ""
        presenter.onGetTermsOfServiceText(termsOfServiceText)
    }

    override fun showRegistrationViews() {
        first_name_text_input.showWithAnimate()
        last_name_text_input.showWithAnimate()
    }

    override fun hideRegistrationViews() {
        first_name_text_input.hideWithAnimate()
        last_name_text_input.hideWithAnimate()
    }

    override fun setMaxLengthOfEditText(expectedCodeLength: Int) {
        enter_auth_code_edit_text.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(expectedCodeLength))
    }

    override fun setupClickListeners() {
        enter_auth_code_next_button.setOnClickListener(::onClickedPhoneNumberNextButton)

        resend_authentication_code_button.setOnClickListener(::onClickedResendAuthenticationCodeButton)

        enter_auth_code_edit_text.setOnFocusChangeListener(::onChangedFocusInPhoneNumberEditText)

        enter_auth_code_edit_text.addTextChangedListener(authCodeTextWatcher)
        first_name_edit_text.addTextChangedListener(firstNameTextWatcher)
        last_name_edit_text.addTextChangedListener(lastNameTextWatcher)
    }

    override fun hideNextButton() {
        enter_auth_code_next_button.hideWithAnimate()
    }

    override fun showNextButton() {
        enter_auth_code_next_button.showWithAnimate()
    }

    override val actionBarTitle: String by lazy { getString(R.string.enter_auth_code_title) }
    override val actionBar: Toolbar
        get() = enter_authentication_code_toolbar as Toolbar

    override val layoutRes: Int = R.layout.fragment_enter_authentication_code

    override val presenter: EnterAuthenticationCodePresenter by lazy { mPresenter }

    @ProvidePresenter
    fun providePresenter(): EnterAuthenticationCodePresenterImpl =
        get(enterAuthenticationCodePresenterQualifier)

    @InjectPresenter
    lateinit var mPresenter: EnterAuthenticationCodePresenterImpl

    private fun onClickedResendAuthenticationCodeButton(
        @Suppress("UNUSED_PARAMETER") view: View
    ) {
        presenter.onResendAuthenticationCodeButtonPressed()
    }

    private fun onClickedPhoneNumberNextButton(
        @Suppress("UNUSED_PARAMETER") view: View
    ) {
        val enteredAuthCode = enter_auth_code_edit_text.text.toString()

        presenter.onNextButtonPressed(
            enteredAuthenticationCode = enteredAuthCode,
            lastName = last_name_edit_text.text.toString(),
            firstName = first_name_edit_text.text.toString()
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

    private val authCodeTextWatcher by lazy {
        TextWatcherImpl(
            onEnteredText = { presenter.onAuthenticationCodeTextChanged(it) }
        )
    }

    private val firstNameTextWatcher by lazy {
        TextWatcherImpl(
            onEnteredText = { presenter.onFirstNameTextChanged(it) }
        )
    }

    private val lastNameTextWatcher by lazy {
        TextWatcherImpl(
            onEnteredText = { presenter.onLastNameTextChanged(it) }
        )
    }

}
