package com.anvipo.angram.coreLayer.base.baseInterfaces

import com.anvipo.angram.presentationLayer.common.interfaces.Presentable
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.google.android.material.snackbar.Snackbar

interface BaseView : Presentable, MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showProgress(): Unit = Unit

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideProgress(): Unit = Unit

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showToastMessage(text: String): Unit = Unit

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackMessage(
        text: String,
        duration: Int = Snackbar.LENGTH_LONG,
        withProgressBar: Boolean = false,
        isProgressBarIndeterminate: Boolean = false
    ): Unit = Unit

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showConnectionErrorSnackMessage(
        text: String,
        duration: Int = Snackbar.LENGTH_LONG,
        withProgressBar: Boolean = false,
        isProgressBarIndeterminate: Boolean = false
    ): Unit = Unit

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showAlertMessage(
        text: String,
        title: String? = null
    ): Unit = Unit

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showErrorAlert(text: String): Unit = Unit

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showItemsDialog(
        title: String? = null,
        items: List<String>,
        tag: String? = null,
        cancelable: Boolean = true
    ): Unit = Unit

}