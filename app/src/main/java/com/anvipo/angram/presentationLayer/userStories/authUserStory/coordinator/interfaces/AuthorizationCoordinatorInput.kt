package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces

import com.anvipo.angram.presentationLayer.common.interfaces.CoordinatorInput

interface AuthorizationCoordinatorInput : CoordinatorInput {
    fun startAuthorizationFlowWithEnterAuthorizationCodeAsRootScreen()
    fun startAuthFlowWithEnterPasswordAsRootScreen()
}
