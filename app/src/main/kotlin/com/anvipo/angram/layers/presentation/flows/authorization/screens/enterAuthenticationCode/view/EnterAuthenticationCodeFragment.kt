package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.view


import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.base.classes.BaseFragment
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters.HIDE
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters.SHOW
import com.anvipo.angram.layers.core.hideKeyboard
import com.anvipo.angram.layers.core.hideWithAnimate
import com.anvipo.angram.layers.core.showWithAnimate
import com.anvipo.angram.layers.core.textWatchers.TextWatcherImpl
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule.enterAuthenticationCodeViewModelQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.types.SetExpectedCodeLengthEventParameters
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.viewModel.EnterAuthenticationCodeViewModel
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.viewModel.EnterAuthenticationCodeViewModelImpl
import kotlinx.android.synthetic.main.fragment_enter_authentication_code.*
import org.koin.android.ext.android.get


class EnterAuthenticationCodeFragment : BaseFragment() {

    companion object {
        fun createNewInstance(
            shouldShowBackButton: Boolean,
            expectedCodeLength: Int,
            enteredPhoneNumber: String,
            registrationRequired: Boolean,
            termsOfServiceText: String
        ): EnterAuthenticationCodeFragment =
            EnterAuthenticationCodeFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_SHOULD_SHOW_BACK_BUTTON, shouldShowBackButton)
                    putBoolean(ARG_REGISTRATION_REQUIRED, registrationRequired)
                    putInt(ARG_EXPECTED_CODE_LENGTH, expectedCodeLength)
                    putString(ARG_ENTERED_PHONE_NUMBER, enteredPhoneNumber)
                    putString(ARG_TERMS_OF_SERVICE_TEXT, termsOfServiceText)
                }
            }

        private const val ARG_EXPECTED_CODE_LENGTH = "arg_expected_code_length"
        private const val ARG_ENTERED_PHONE_NUMBER = "arg_entered_phone_number"
        private const val ARG_REGISTRATION_REQUIRED = "arg_registration_required"
        private const val ARG_TERMS_OF_SERVICE_TEXT = "arg_terms_of_service_text"
    }

    override val actionBarTitle: String by lazy { getString(R.string.enter_auth_code_title) }

    override val actionBar: Toolbar
        get() = enter_authentication_code_toolbar as Toolbar
    override val layoutRes: Int = R.layout.fragment_enter_authentication_code

    override val viewModel: EnterAuthenticationCodeViewModel
            by viewModels<EnterAuthenticationCodeViewModelImpl> {
                get(enterAuthenticationCodeViewModelQualifier)
            }

    @ExperimentalUnsignedTypes
    override fun extractDataFromBundle() {
        val expectedCodeLength = arguments?.getInt(ARG_EXPECTED_CODE_LENGTH) ?: 5
        viewModel.onGetExpectedCodeLength(expectedCodeLength.toUInt())

        val enteredPhoneNumber = arguments?.getString(ARG_ENTERED_PHONE_NUMBER) ?: ""
        viewModel.onGetEnteredPhoneNumber(enteredPhoneNumber)

        val registrationRequired = arguments?.getBoolean(ARG_REGISTRATION_REQUIRED) ?: false
        viewModel.onGetRegistrationRequired(registrationRequired)

        val termsOfServiceText = arguments?.getString(ARG_TERMS_OF_SERVICE_TEXT) ?: ""
        viewModel.onGetTermsOfServiceText(termsOfServiceText)
    }

    override fun setupClickListeners() {
        enter_auth_code_next_button.setOnClickListener(::onClickedPhoneNumberNextButton)

        resend_authentication_code_button.setOnClickListener(::onClickedResendAuthenticationCodeButton)

        enter_auth_code_edit_text.setOnFocusChangeListener(::onChangedFocusInPhoneNumberEditText)

        enter_auth_code_edit_text.addTextChangedListener(authCodeTextWatcher)
        first_name_edit_text.addTextChangedListener(firstNameTextWatcher)
        last_name_edit_text.addTextChangedListener(lastNameTextWatcher)
    }

    override fun setupViewModelsObservers() {
        super.setupViewModelsObservers()
        viewModel
            .showNextButtonEvents
            .observe(this) {
                @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
                when (it) {
                    SHOW -> showNextButton()
                    HIDE -> hideNextButton()
                }
            }

        viewModel
            .showRegistrationViewsEvents
            .observe(this) {
                @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
                when (it) {
                    SHOW -> showRegistrationViews()
                    HIDE -> hideRegistrationViews()
                }
            }

        viewModel
            .setExpectedCodeLengthEvents
            .observe(this) {
                setMaxLengthOfEditText(it)
            }
    }


    private val authCodeTextWatcher by lazy {
        TextWatcherImpl(
            onEnteredText = { viewModel.onAuthenticationCodeTextChanged(it) }
        )
    }

    private val firstNameTextWatcher by lazy {
        TextWatcherImpl(
            onEnteredText = { viewModel.onFirstNameTextChanged(it) }
        )
    }

    private val lastNameTextWatcher by lazy {
        TextWatcherImpl(
            onEnteredText = { viewModel.onLastNameTextChanged(it) }
        )
    }

    private fun showNextButton() {
        enter_auth_code_next_button.showWithAnimate()
    }

    private fun hideNextButton() {
        enter_auth_code_next_button.hideWithAnimate()
    }

    private fun showRegistrationViews() {
        first_name_text_input.showWithAnimate()
        last_name_text_input.showWithAnimate()
    }

    private fun hideRegistrationViews() {
        first_name_text_input.hideWithAnimate()
        last_name_text_input.hideWithAnimate()
    }

    private fun setMaxLengthOfEditText(expectedCodeLength: SetExpectedCodeLengthEventParameters) {
        val lengthFilter = InputFilter.LengthFilter(expectedCodeLength.expectedCodeLength)
        enter_auth_code_edit_text.filters = arrayOf<InputFilter>(lengthFilter)
    }

    private fun onClickedResendAuthenticationCodeButton(
        @Suppress("UNUSED_PARAMETER") view: View
    ) {
        viewModel.onResendAuthenticationCodeButtonPressed()
    }

    private fun onClickedPhoneNumberNextButton(
        @Suppress("UNUSED_PARAMETER") view: View
    ) {
        val enteredAuthCode = enter_auth_code_edit_text.text.toString()

        viewModel.onNextButtonPressed(
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

}
