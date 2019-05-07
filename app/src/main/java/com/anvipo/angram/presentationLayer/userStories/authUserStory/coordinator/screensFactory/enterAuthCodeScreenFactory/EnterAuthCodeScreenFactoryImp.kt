package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory.enterAuthCodeScreenFactory

import androidx.fragment.app.Fragment
import com.anvipo.angram.applicationLayer.di.BackButtonPressedType
import com.anvipo.angram.applicationLayer.di.CorrectAuthCodeType
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view.EnterAuthCodeFragment
import kotlinx.coroutines.channels.ReceiveChannel
import ru.terrakok.cicerone.android.support.SupportAppScreen

class EnterAuthCodeScreenFactoryImp(
    private val enteredCorrectAuthCodeReceiveChannel: ReceiveChannel<CorrectAuthCodeType>,
    private val backButtonPressedInAuthCodeScreenReceiveChannel: ReceiveChannel<BackButtonPressedType>
) : EnterAuthCodeScreenFactory {

    override fun createEnterAuthCodeViewController(
        tdLibGateway: TDLibGateway
    ): Triple<SupportAppScreen, ReceiveChannel<CorrectAuthCodeType>, ReceiveChannel<BackButtonPressedType>> {
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