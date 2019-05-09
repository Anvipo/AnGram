package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.presenter

import com.anvipo.angram.presentationLayer.common.interfaces.BasePresenter
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.types.CorrectAuthCodeType

interface EnterAuthCodePresenter : BasePresenter {
    fun onNextButtonPressed(enteredAuthCode: CorrectAuthCodeType)
    fun onAuthCodeTextChanged(text: CharSequence?)

    fun onGetExpectedCodeLength(expectedCodeLength: UInt)
    fun onGetEnteredPhoneNumber(enteredPhoneNumber: String)
}