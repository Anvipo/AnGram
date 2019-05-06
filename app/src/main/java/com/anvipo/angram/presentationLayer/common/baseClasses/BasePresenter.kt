package com.anvipo.angram.presentationLayer.common.baseClasses

import com.anvipo.angram.presentationLayer.common.interfaces.BaseView
import com.anvipo.angram.presentationLayer.common.interfaces.IBasePresenter
import com.arellomobile.mvp.MvpPresenter

abstract class BasePresenter<V : BaseView> : MvpPresenter<V>(), IBasePresenter {

    // TODO: coroutines dispose etc

}