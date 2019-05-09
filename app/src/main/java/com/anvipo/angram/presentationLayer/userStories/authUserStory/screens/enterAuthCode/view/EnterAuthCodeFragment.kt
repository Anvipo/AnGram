package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view


import android.os.Bundle
import android.text.Editable
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

        @JvmStatic
        fun createNewInstance(shouldShowBackButton: Boolean = false): EnterAuthCodeView =
            EnterAuthCodeFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_SHOULD_SHOW_BACK_BUTTON, shouldShowBackButton)
                }
            }
    }

    override fun extractDataFromBundle() {
        shouldShowBackButton = arguments?.getBoolean(ARG_SHOULD_SHOW_BACK_BUTTON) ?: false
    }

    override fun setupClickListeners() {
        enter_auth_code_next_button.setOnClickListener(::onClickedPhoneNumberNextButton)

        enter_auth_code_edit_text.setOnFocusChangeListener(::onChangedFocusInPhoneNumberEditText)

        enter_auth_code_edit_text.addTextChangedListener(authCodeTextWatcher)
    }

    override fun hideNextButton() {
        TODO("not implemented")
    }

    override fun showNextButton() {
        TODO("not implemented")
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
