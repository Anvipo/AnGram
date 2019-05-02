package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.signIn.view


import com.anvipo.angram.R
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseFragment

class SignInFragment : BaseFragment(), SignInView {

    override var onBackPressed: (() -> Unit)? = null

    override val layoutRes: Int = R.layout.fragment_sign_in


    companion object {
        @JvmStatic
        fun createNewInstance(): SignInView {
            return SignInFragment()
        }
    }
}
