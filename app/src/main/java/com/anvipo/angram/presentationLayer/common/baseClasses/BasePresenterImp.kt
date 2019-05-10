package com.anvipo.angram.presentationLayer.common.baseClasses

import androidx.fragment.app.Fragment
import com.anvipo.angram.businessLogicLayer.useCases.base.BaseUseCase
import com.anvipo.angram.coreLayer.CoreHelpers.assertionFailure
import com.anvipo.angram.coreLayer.base.baseInterfaces.BaseView
import com.anvipo.angram.presentationLayer.common.interfaces.BasePresenter
import com.anvipo.angram.presentationLayer.common.interfaces.CoordinatorOutput
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.CoroutineScope

abstract class BasePresenterImp<V : BaseView> : MvpPresenter<V>(), BasePresenter, CoroutineScope {

    protected abstract val coordinator: CoordinatorOutput
    protected abstract val useCase: BaseUseCase

    override fun onDestroy() {
        cancelAllJobs()
        super.onDestroy()
    }

    protected abstract fun cancelAllJobs()

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