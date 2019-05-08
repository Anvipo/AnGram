package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator

import com.anvipo.angram.presentationLayer.common.interfaces.CoordinatorOutput
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.types.CorrectPhoneNumberType

interface AuthorizationCoordinatorOutput : CoordinatorOutput {

    fun onPressedBackButtonInEnterPhoneNumberScreen()
    fun onEnterCorrectPhoneNumber(phoneNumber: CorrectPhoneNumberType)

}