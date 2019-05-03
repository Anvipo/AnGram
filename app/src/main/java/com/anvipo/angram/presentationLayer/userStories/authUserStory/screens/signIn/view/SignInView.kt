package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.signIn.view

import com.anvipo.angram.presentationLayer.common.interfaces.Presentable

interface SignInView : Presentable {

    var onFinishFlow: (() -> Unit)?

}