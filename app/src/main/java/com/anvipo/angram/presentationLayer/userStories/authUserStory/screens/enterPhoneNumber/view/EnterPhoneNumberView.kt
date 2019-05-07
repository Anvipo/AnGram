package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view

import com.anvipo.angram.presentationLayer.common.interfaces.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface EnterPhoneNumberView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showErrorAlert(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showLoading()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideLoading()

}
