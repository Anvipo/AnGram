package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.authorizationOptions.view


import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.R
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseFragment
import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.fragment_authorization_options.*

class AuthorizationOptionsFragment : BaseFragment(), AuthorizationOptionsView {

    companion object {
        fun createNewInstance(): AuthorizationOptionsView {
            return AuthorizationOptionsFragment()
        }
    }

    override var onSignIn: (() -> Unit)? = null
    override var onSignUp: (() -> Unit)? = null

    override val layoutRes: Int = R.layout.fragment_authorization_options

    override val actionBarTitle: String = "Authorization options"
    override val actionBarSubitle: String = ""
    override val actionBar: Toolbar
        get() = toolbar


    override fun setupClickListeners() {
        sign_in_button.setOnClickListener {
            onSignIn?.invoke()
        }
    }

}
