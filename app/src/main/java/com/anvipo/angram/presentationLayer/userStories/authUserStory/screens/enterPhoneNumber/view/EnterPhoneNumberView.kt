package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view

import com.anvipo.angram.presentationLayer.common.interfaces.Presentable
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenter

interface EnterPhoneNumberView : Presentable {

    fun showErrorAlert(message: String)

    var onEnteredCorrectPhoneNumber: ((String) -> Unit)?

    // TODO: apply DI
    var presenter: EnterPhoneNumberPresenter

}