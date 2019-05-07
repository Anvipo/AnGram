package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory

import androidx.fragment.app.Fragment
import com.anvipo.angram.applicationLayer.di.CorrectAuthCodeReceiveChannel
import com.anvipo.angram.applicationLayer.types.BackButtonPressedReceiveChannel
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view.EnterAuthCodeFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class EnterAuthCodeScreenFactoryImp(
    private val enteredCorrectAuthCodeReceiveChannel: CorrectAuthCodeReceiveChannel,
    private val backButtonPressedInAuthCodeScreenReceiveChannel: BackButtonPressedReceiveChannel
) : EnterAuthCodeScreenFactory {

    override fun createEnterAuthCodeViewController(
        tdLibGateway: TDLibGateway
    ): Triple<SupportAppScreen, CorrectAuthCodeReceiveChannel, BackButtonPressedReceiveChannel> {
        val enterAuthCodeScreen = object : SupportAppScreen() {
            override fun getFragment(): Fragment = EnterAuthCodeFragment.createNewInstance() as Fragment
        }

        return Triple(
            enterAuthCodeScreen,
            enteredCorrectAuthCodeReceiveChannel,
            backButtonPressedInAuthCodeScreenReceiveChannel
        )
    }

}