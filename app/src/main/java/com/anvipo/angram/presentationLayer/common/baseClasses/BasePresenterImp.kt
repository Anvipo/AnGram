package com.anvipo.angram.presentationLayer.common.baseClasses

import com.anvipo.angram.businessLogicLayer.useCases.base.BaseUseCase
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

}