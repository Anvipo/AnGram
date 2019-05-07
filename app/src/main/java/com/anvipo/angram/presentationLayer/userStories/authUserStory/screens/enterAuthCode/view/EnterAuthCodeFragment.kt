package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view


import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.R
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseFragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.presenter.EnterAuthCodePresenter
import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.fragment_enter_auth_code.*
import org.koin.android.ext.android.inject

class EnterAuthCodeFragment : BaseFragment(), EnterAuthCodeView {

    companion object {
        @JvmStatic
        fun createNewInstance(): EnterAuthCodeView {
            return EnterAuthCodeFragment()
        }
    }

    override fun setupClickListeners() {
        enter_auth_code_next_button.setOnClickListener {
            val enteredAuthCode = enter_auth_code_edit_text.text.toString()

//            presenter.onNextButtonPressed(enteredPhoneNumber)
        }

    }

    override val actionBarTitle: String = "Enter auth code"

    override val actionBarSubitle: String = ""
    override val actionBar: Toolbar
        get() = toolbar
    override val layoutRes: Int = R.layout.fragment_enter_auth_code

    override val presenter: EnterAuthCodePresenter by inject()

}
