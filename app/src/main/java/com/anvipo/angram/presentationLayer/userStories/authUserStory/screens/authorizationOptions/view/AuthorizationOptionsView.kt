package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.authorizationOptions.view

import com.anvipo.angram.presentationLayer.common.interfaces.Presentable

interface AuthorizationOptionsView : Presentable {

    var onSignIn: (() -> Unit)?
    var onSignUp: (() -> Unit)?

}