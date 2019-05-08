package com.anvipo.angram.coreLayer.base.baseInterfaces

import com.anvipo.angram.presentationLayer.common.interfaces.Presentable
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface BaseView : Presentable, MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showProgress(): Unit = Unit

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideProgress(): Unit = Unit

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showToastMessage(text: String): Unit = Unit

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackMessage(text: String): Unit = Unit

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showAlertMessage(text: String): Unit = Unit

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showErrorAlert(text: String): Unit = Unit

}