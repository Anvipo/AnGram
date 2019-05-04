package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view


import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.R
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseFragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenter
import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*

class EnterPhoneNumberFragment : BaseFragment(), EnterPhoneNumberView {

    override fun showErrorAlert(message: String) {
        val context = context ?: return

        // TODO: translate strings
        AlertDialog.Builder(context)
            .setTitle("Error")
            .setMessage(message)
            .setNeutralButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }


    override var onEnteredCorrectPhoneNumber: ((String) -> Unit)? = null

    companion object {
        @JvmStatic
        fun createNewInstance(): EnterPhoneNumberView {
            return EnterPhoneNumberFragment()
        }
    }

    override val layoutRes: Int = R.layout.fragment_enter_phone_number

    override lateinit var presenter: EnterPhoneNumberPresenter

    override val actionBarTitle: String = "Enter your phone number"
    override val actionBarSubitle: String = ""
    override val actionBar: Toolbar
        get() = toolbar

    override fun setupClickListeners() {
        enter_phone_number_next_button.setOnClickListener {
            val enteredPhoneNumber = enter_phone_number_edit_text.text.toString()

            presenter.didTapNextButton(enteredPhoneNumber)
        }
    }

}
