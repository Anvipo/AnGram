package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.presenter

import com.anvipo.angram.presentationLayer.common.interfaces.BasePresenter
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.types.CorrectAuthCodeType

interface EnterAuthCodePresenter : BasePresenter {
    fun onNextButtonPressed(enteredPhoneNumber: CorrectAuthCodeType)
    fun onAuthCodeTextChanged(text: CharSequence?)
}