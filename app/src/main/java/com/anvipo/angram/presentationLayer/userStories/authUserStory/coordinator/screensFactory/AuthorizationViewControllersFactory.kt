package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory

import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.coreLayer.message.IReceiveDataNotifier
import ru.terrakok.cicerone.android.support.SupportAppScreen

typealias SupportAppScreenAndIReceiveDataNotifierPair =
        Triple<SupportAppScreen, IReceiveDataNotifier<String>, IReceiveDataNotifier<Unit>>

interface AuthorizationViewControllersFactory {

    fun createEnterPhoneNumberViewController(tdLibGateway: TDLibGateway): SupportAppScreenAndIReceiveDataNotifierPair

    fun createEnterAuthCodeViewController(tdLibGateway: TDLibGateway): SupportAppScreenAndIReceiveDataNotifierPair

}