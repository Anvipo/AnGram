package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.signIn.view


import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.R
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseFragment
import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : BaseFragment(), SignInView {

    companion object {
        @JvmStatic
        fun createNewInstance(): SignInView {
            return SignInFragment()
        }
    }

    override var onFinishFlow: (() -> Unit)? = null

    override val layoutRes: Int = R.layout.fragment_sign_in

    override val actionBarTitle: String = "Sign in"
    override val actionBarSubitle: String = ""
    override val actionBar: Toolbar
        get() = toolbar

    override fun setupClickListeners() {
        sign_in_ff_button.setOnClickListener {
            onFinishFlow?.invoke()
        }
    }

}
