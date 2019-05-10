package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view


import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.R
import com.anvipo.angram.coreLayer.base.baseClasses.BaseFragment
import com.anvipo.angram.coreLayer.hideKeyboard
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.di.EnterAuthCodeModule.enterAuthCodePresenter
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.presenter.EnterAuthCodePresenter
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.presenter.EnterAuthCodePresenterImp
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.fragment_enter_auth_code.*
import org.koin.android.ext.android.get


class EnterAuthCodeFragment : BaseFragment(), EnterAuthCodeView {

    companion object {
        private const val ARG_SHOULD_SHOW_BACK_BUTTON = "arg_should_show_back_button"
        private const val ARG_EXPECTED_CODE_LENGTH = "arg_expected_code_length"
        private const val ARG_ENTERED_PHONE_NUMBER = "arg_entered_phone_number"

        @JvmStatic
        fun createNewInstance(
            shouldShowBackButton: Boolean,
            expectedCodeLength: Int,
            enteredPhoneNumber: String
        ): EnterAuthCodeView =
            EnterAuthCodeFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_SHOULD_SHOW_BACK_BUTTON, shouldShowBackButton)
                    putInt(ARG_EXPECTED_CODE_LENGTH, expectedCodeLength)
                    putString(ARG_ENTERED_PHONE_NUMBER, enteredPhoneNumber)
                }
            }
    }

    override fun extractDataFromBundle() {
        shouldShowBackButton = arguments?.getBoolean(ARG_SHOULD_SHOW_BACK_BUTTON) ?: false

        val expectedCodeLength = arguments?.getInt(ARG_EXPECTED_CODE_LENGTH) ?: 5
        presenter.onGetExpectedCodeLength(expectedCodeLength.toUInt())

        val enteredPhoneNumber = arguments?.getString(ARG_ENTERED_PHONE_NUMBER) ?: ""
        presenter.onGetEnteredPhoneNumber(enteredPhoneNumber)
    }

    override fun setMaxLengthOfEditText(expectedCodeLength: Int) {
        enter_auth_code_edit_text.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(expectedCodeLength))
    }

    override fun setupClickListeners() {
        enter_auth_code_next_button.setOnClickListener(::onClickedPhoneNumberNextButton)

        enter_auth_code_edit_text.setOnFocusChangeListener(::onChangedFocusInPhoneNumberEditText)

        enter_auth_code_edit_text.addTextChangedListener(authCodeTextWatcher)
    }

    override fun hideNextButton() {
        enter_auth_code_next_button.visibility = View.INVISIBLE
    }

    override fun showNextButton() {
        enter_auth_code_next_button.visibility = View.VISIBLE
    }

    override val actionBarTitle: String by lazy { getString(R.string.enter_auth_code_title) }
    override val actionBar: Toolbar by lazy { toolbar }

    override val layoutRes: Int = R.layout.fragment_enter_auth_code

    override val presenter: EnterAuthCodePresenter  by lazy { mPresenter }

    @Suppress("ProtectedInFinal")
    @ProvidePresenter
    protected fun providePresenter(): EnterAuthCodePresenterImp = get(enterAuthCodePresenter)

    @InjectPresenter
    internal lateinit var mPresenter: EnterAuthCodePresenterImp

    private fun onClickedPhoneNumberNextButton(
        @Suppress("UNUSED_PARAMETER") view: View
    ) {
        val enteredAuthCode = enter_auth_code_edit_text.text.toString()

        presenter.onNextButtonPressed(enteredAuthCode)
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
        object : TextWatcher {
            override fun afterTextChanged(editText: Editable?): Unit = Unit

            override fun beforeTextChanged(
                text: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ): Unit = Unit

            override fun onTextChanged(
                text: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                presenter.onAuthCodeTextChanged(text)
            }
        }
    }

}
