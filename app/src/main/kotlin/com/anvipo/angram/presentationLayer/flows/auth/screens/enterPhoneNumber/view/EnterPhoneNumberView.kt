package com.anvipo.angram.presentationLayer.flows.auth.screens.enterPhoneNumber.view

import com.anvipo.angram.coreLayer.base.interfaces.BaseView
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
