package com.anvipo.angram.presentationLayer.common.baseClasses

import com.anvipo.angram.businessLogicLayer.useCases.base.BaseUseCase
import com.anvipo.angram.presentationLayer.common.interfaces.BasePresenter
import com.anvipo.angram.presentationLayer.common.interfaces.BaseView
import com.anvipo.angram.presentationLayer.common.interfaces.Coordinatorable
import com.arellomobile.mvp.MvpPresenter

abstract class BasePresenterImp<V : BaseView> : MvpPresenter<V>(), BasePresenter {

    protected abstract val coordinator: Coordinatorable
    protected abstract val useCase: BaseUseCase

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

}