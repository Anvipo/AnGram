package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces

import com.anvipo.angram.presentationLayer.common.interfaces.BaseRouteEventHandler

interface AuthorizationCoordinatorEnterPhoneNumberRouteEventHandler : BaseRouteEventHandler {

    fun onPressedBackButtonInEnterPhoneNumberScreen()
    fun onEnterCorrectPhoneNumber()

}
