package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces

import com.anvipo.angram.presentationLayer.common.interfaces.CoordinatorOutput

interface AuthorizationCoordinatorEnterAuthenticationCodeOutput : CoordinatorOutput {

    fun onPressedBackButtonInEnterAuthenticationCodeScreen()
    fun onEnterCorrectAuthenticationCode()

}