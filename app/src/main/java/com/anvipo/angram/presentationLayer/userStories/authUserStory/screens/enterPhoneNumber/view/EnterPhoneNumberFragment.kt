package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view


import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.R
import com.anvipo.angram.coreLayer.MessageDialogFragment
import com.anvipo.angram.coreLayer.base.baseClasses.BaseFragment
import com.anvipo.angram.coreLayer.hideKeyboard
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.di.EnterPhoneNumberModule.enterPhoneNumberPresenter
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenter
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenterImp
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import org.koin.android.ext.android.get

class EnterPhoneNumberFragment :
    BaseFragment(),
    EnterPhoneNumberView,
    MessageDialogFragment.OnClickListener {

    companion object {
        @JvmStatic
        fun createNewInstance(): EnterPhoneNumberView = EnterPhoneNumberFragment()
    }

    override fun hideNextButton() {
        enter_phone_number_next_button.visibility = View.INVISIBLE
    }

    override fun showNextButton() {
        enter_phone_number_next_button.visibility = View.VISIBLE
    }

    override fun askForReadPhoneStatePermission(withNoOption: Boolean) {
        val text = getString(R.string.please_provide_read_phone_state)

        MessageDialogFragment
            .create(
                message = text,
                positive = getString(android.R.string.ok),
                negative = if (withNoOption) getString(R.string.no) else null,
                cancelable = false,
                tag = DialogTags.AskForReadPhoneStatePermissionTag.tag
            )
            .show(childFragmentManager, null)
    }

    override fun dialogPositiveClicked(tag: String) {
        when (tag) {
            DialogTags.AskForReadPhoneStatePermissionTag.tag ->
                presenter.onAskForReadPhoneStatePermissionPositiveClick()
            DialogTags.AskForGoToSettingsForReadPhoneStatePermissionTag.tag -> goToSettings()
        }
    }

    override fun askForGoToSettingsForReadPhoneStatePermission() {
        val part1 =
            getString(R.string.go_to_applications_settings_and_provide_read_phone_state_permission)
        val part2 = getString(R.string.go_to)

        val text = "$part1 $part2?"

        MessageDialogFragment
            .create(
                message = text,
                positive = getString(android.R.string.ok),
                negative = getString(R.string.no),
                cancelable = false,
                tag = DialogTags.AskForGoToSettingsForReadPhoneStatePermissionTag.tag
            )
            .show(childFragmentManager, null)
    }

    override fun setMaxLengthOfPhoneNumber(maxLength: Int) {
        enter_phone_number_edit_text.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
    }

    override val presenter: EnterPhoneNumberPresenter by lazy { mPresenter }

    override val layoutRes: Int by lazy { R.layout.fragment_enter_phone_number }

    override val actionBarTitle: String by lazy { getString(R.string.enter_your_phone_number_title) }
    override val actionBar: Toolbar by lazy { toolbar }

    override fun setupClickListeners() {
        enter_phone_number_next_button.setOnClickListener(::onClickedPhoneNumberNextButton)

        enter_phone_number_edit_text.setOnFocusChangeListener(::onChangedFocusInPhoneNumberEditText)

        enter_phone_number_edit_text.addTextChangedListener(phoneNumberTextWatcher)
    }

    @Suppress("ProtectedInFinal")
    @ProvidePresenter
    protected fun providePresenter(): EnterPhoneNumberPresenterImp =
        get(enterPhoneNumberPresenter)

    @InjectPresenter
    internal lateinit var mPresenter: EnterPhoneNumberPresenterImp

    private val phoneNumberTextWatcher by lazy {
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
                presenter.onPhoneNumberTextChanged(text)
            }
        }
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

    private fun onClickedPhoneNumberNextButton(
        @Suppress("UNUSED_PARAMETER") view: View
    ) {
        val enteredPhoneNumber = enter_phone_number_edit_text.text.toString()

        presenter.onNextButtonPressed(enteredPhoneNumber)
    }

    private enum class DialogTags(val tag: String) {
        AskForReadPhoneStatePermissionTag("ask_for_read_phone_state_permission"),
        AskForGoToSettingsForReadPhoneStatePermissionTag("ask_for_go_to_settings_for_read_phone_state_permission")
    }

}
