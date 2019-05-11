package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces

import com.anvipo.angram.presentationLayer.common.interfaces.CoordinatorOutput

interface AuthorizationCoordinatorEnterAuthenticationPasswordOutput : CoordinatorOutput {

    fun onPressedBackButtonInEnterAuthenticationPasswordScreen()
    fun onEnterCorrectAuthenticationPassword()

}