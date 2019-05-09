package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces

import com.anvipo.angram.presentationLayer.common.interfaces.CoordinatorOutput
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.types.CorrectPhoneNumberType

interface AuthorizationCoordinatorEnterPhoneNumberOutput : CoordinatorOutput {

    fun onPressedBackButtonInEnterPhoneNumberScreen()
    fun onEnterCorrectPhoneNumber(phoneNumber: CorrectPhoneNumberType)

}
