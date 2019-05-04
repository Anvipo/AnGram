package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory

import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view.EnterAuthCodeView
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberView
import ru.terrakok.cicerone.android.support.SupportAppScreen

interface AuthorizationViewControllersFactory {

    fun createEnterPhoneNumberViewController(tdLibGateway: TDLibGateway): Pair<EnterPhoneNumberView, SupportAppScreen>

    fun createEnterAuthCodeViewController(tdLibGateway: TDLibGateway): Pair<EnterAuthCodeView, SupportAppScreen>

}