package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view

import com.anvipo.angram.coreLayer.base.baseInterfaces.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface EnterAuthCodeView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideNextButton()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showNextButton()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setMaxLengthOfEditText(expectedCodeLength: Int)

}