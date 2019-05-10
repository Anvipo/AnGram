package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces

import com.anvipo.angram.presentationLayer.common.interfaces.CoordinatorOutput

interface AuthorizationCoordinatorEnterAuthCodeOutput : CoordinatorOutput {

    fun onPressedBackButtonInEnterAuthCodeScreen()
    fun onEnterCorrectAuthCode()

}