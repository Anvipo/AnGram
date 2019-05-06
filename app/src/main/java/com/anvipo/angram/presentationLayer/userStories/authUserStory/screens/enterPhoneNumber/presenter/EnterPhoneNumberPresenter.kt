package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter

import com.anvipo.angram.presentationLayer.common.interfaces.IBasePresenter

interface EnterPhoneNumberPresenter : IBasePresenter {

    fun didTapNextButton(enteredPhoneNumber: String)

}