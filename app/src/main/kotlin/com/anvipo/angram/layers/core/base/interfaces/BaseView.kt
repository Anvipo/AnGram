package com.anvipo.angram.layers.core.base.interfaces

import com.anvipo.angram.layers.presentation.common.interfaces.Presentable
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
        duration: Int = Snackbar.LENGTH_LONG
    ): Unit = Unit

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showConnectionStateSnackMessage(
        text: String,
        duration: Int = Snackbar.LENGTH_LONG
    ): Unit = Unit

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showAlertMessage(
        text: String,
        title: String? = null,
        cancelable: Boolean = true,
        messageDialogTag: String = ""
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