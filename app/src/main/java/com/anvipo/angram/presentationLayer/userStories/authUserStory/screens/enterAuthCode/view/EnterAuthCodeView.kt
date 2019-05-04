package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view

import com.anvipo.angram.presentationLayer.common.interfaces.Presentable
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.presenter.EnterAuthCodePresenter

interface EnterAuthCodeView : Presentable {

    // TODO: apply DI
    var presenter: EnterAuthCodePresenter

}