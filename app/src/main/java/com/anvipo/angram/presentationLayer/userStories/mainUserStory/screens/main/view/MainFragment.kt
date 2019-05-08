package com.anvipo.angram.presentationLayer.userStories.mainUserStory.screens.main.view


import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.R
import com.anvipo.angram.coreLayer.base.baseClasses.BaseFragment
import com.anvipo.angram.presentationLayer.common.interfaces.BasePresenter
import kotlinx.android.synthetic.main.appbar.*

class MainFragment : BaseFragment(), MainView {

    override val presenter: BasePresenter
        get() = TODO("not implemented")

    companion object {
        @JvmStatic
        fun createNewInstance(): MainView {
            return MainFragment()
        }
    }

    override fun setupClickListeners() {}

    override val actionBarTitle: String = "Main"
    override val actionBarSubtitle: String = ""
    override val actionBar: Toolbar
        get() = toolbar

    override val layoutRes: Int = R.layout.fragment_main

}
