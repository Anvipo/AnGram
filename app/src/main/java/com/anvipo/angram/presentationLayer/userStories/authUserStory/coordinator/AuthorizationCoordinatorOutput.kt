package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator

interface AuthorizationCoordinatorOutput {

    var finishFlow: (() -> Unit)?

}