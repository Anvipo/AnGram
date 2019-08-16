package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterPhoneNumber.view

import com.anvipo.angram.layers.core.base.interfaces.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface EnterPhoneNumberView : BaseView {

    fun enableNextButton()

    fun disableNextButton()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showNextButton()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideNextButton()

}
