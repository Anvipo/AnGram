package com.anvipo.angram.presentationLayer.userStories.mainUserStory.screens.main.view


import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.R
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseFragment
import kotlinx.android.synthetic.main.appbar.*

class MainFragment : BaseFragment(), MainView {

    companion object {
        @JvmStatic
        fun createNewInstance(): MainView {
            return MainFragment()
        }
    }

    override fun setupClickListeners() {}

    override val actionBarTitle: String = "Main"
    override val actionBarSubitle: String = ""
    override val actionBar: Toolbar
        get() = toolbar

    override val layoutRes: Int = R.layout.fragment_main

}
