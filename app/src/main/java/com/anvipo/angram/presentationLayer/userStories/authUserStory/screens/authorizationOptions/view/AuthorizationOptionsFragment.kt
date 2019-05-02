package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.authorizationOptions.view


import android.os.Bundle
import com.anvipo.angram.R
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseFragment
import kotlinx.android.synthetic.main.fragment_authorization_options.*

class AuthorizationOptionsFragment : BaseFragment(), AuthorizationOptionsView {

    override var onBackPressed: (() -> Unit)? = null

    override var onSignIn: (() -> Unit)? = null
    override var onSignUp: (() -> Unit)? = null


    override val layoutRes: Int = R.layout.fragment_authorization_options

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupClickListeners()

    }

    private fun setupClickListeners() {
        sign_in_button.setOnClickListener {
            onSignIn?.invoke()
        }


    }


    companion object {
        fun createNewInstance(): AuthorizationOptionsView {
            return AuthorizationOptionsFragment()
        }
    }

}
