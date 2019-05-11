package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationCode.view

import com.anvipo.angram.coreLayer.base.baseInterfaces.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface EnterAuthenticationCodeView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideNextButton()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showNextButton()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setMaxLengthOfEditText(expectedCodeLength: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showRegistrationViews()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideRegistrationViews()

}