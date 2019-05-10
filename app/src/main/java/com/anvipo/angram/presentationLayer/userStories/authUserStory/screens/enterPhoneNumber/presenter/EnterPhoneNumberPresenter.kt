package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter

import com.anvipo.angram.presentationLayer.common.interfaces.BasePresenter

interface EnterPhoneNumberPresenter : BasePresenter {

    fun onNextButtonPressed(enteredPhoneNumber: String)

    fun onPhoneNumberTextChanged(text: CharSequence?)

}