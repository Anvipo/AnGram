package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view

import com.anvipo.angram.coreLayer.base.baseInterfaces.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface EnterPhoneNumberView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideNextButton()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showNextButton()

    fun setMaxLengthOfPhoneNumber(maxLength: Int)

}
