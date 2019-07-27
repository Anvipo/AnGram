package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces

import com.anvipo.angram.presentationLayer.common.interfaces.BaseCoordinator

interface AuthorizationCoordinator : BaseCoordinator {

    fun startAuthorizationFlowWithEnterAuthorizationCodeAsRootScreen()
    fun startAuthFlowWithEnterPasswordAsRootScreen()

}
