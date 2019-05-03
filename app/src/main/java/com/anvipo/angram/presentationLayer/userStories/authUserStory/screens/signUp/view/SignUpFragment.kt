package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.signUp.view


import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.R
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseFragment
import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : BaseFragment(), SignUpView {

    companion object {
        @JvmStatic
        fun createNewInstance(): SignUpView {
            return SignUpFragment()
        }
    }

    override lateinit var onFinishFlow: () -> Unit

    override val layoutRes: Int = R.layout.fragment_sign_up

    override val actionBarTitle: String = "Sign up"
    override val actionBarSubitle: String = ""
    override val actionBar: Toolbar
        get() = toolbar

    override fun setupClickListeners() {
        sign_up_ff_button.setOnClickListener {
            onFinishFlow()
        }
    }

}
