package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator

import com.anvipo.angram.presentationLayer.common.interfaces.CoordinatorOutput

interface AuthorizationCoordinatorOutput : CoordinatorOutput {

    fun onPressedBackButtonInEnterPhoneNumberScreen()
    fun onEnterCorrectPhoneNumber(phoneNumber: String)

}