package com.anvipo.angram.presentationLayer.common.baseClasses

import com.anvipo.angram.coreLayer.base.baseInterfaces.BaseView
import com.anvipo.angram.presentationLayer.common.interfaces.BasePresenter
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class BasePresenterImp<V : BaseView> :
    MvpPresenter<V>(),
    BasePresenter,
    CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Default

    override fun onDestroy() {
        cancelAllJobs()
        super.onDestroy()
    }

    protected open fun cancelAllJobs(): Unit = Unit

}