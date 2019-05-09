package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces

import com.anvipo.angram.presentationLayer.common.interfaces.CoordinatorOutput
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.types.CorrectAuthCodeType

interface AuthorizationCoordinatorEnterAuthCodeOutput : CoordinatorOutput {

    fun onPressedBackButtonInEnterAuthCodeScreen()
    fun onEnterCorrectAuthCode(authCode: CorrectAuthCodeType)

}