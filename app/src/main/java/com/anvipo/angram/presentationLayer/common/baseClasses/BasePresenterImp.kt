package com.anvipo.angram.presentationLayer.common.baseClasses

import androidx.fragment.app.Fragment
import com.anvipo.angram.coreLayer.CoreHelpers.assertionFailure
import com.anvipo.angram.coreLayer.base.baseInterfaces.BaseView
import com.anvipo.angram.presentationLayer.common.interfaces.BasePresenter
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class BasePresenterImp<V : BaseView> : MvpPresenter<V>(), BasePresenter, CoroutineScope {

    override fun onDestroy() {
        cancelAllJobs()
        super.onDestroy()
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Default

    protected open fun cancelAllJobs() {}

    protected val currentFragment: Fragment?
        get() {
            val attachedViews = attachedViews

            val view = attachedViews.firstOrNull { it is V }

            if (view == null) {
                assertionFailure("view == null")
                return null
            }

            return view as? Fragment
        }

}