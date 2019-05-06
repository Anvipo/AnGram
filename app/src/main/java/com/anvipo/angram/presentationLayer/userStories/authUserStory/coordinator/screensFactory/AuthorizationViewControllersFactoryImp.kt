package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory

import androidx.fragment.app.Fragment
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.coreLayer.message.IReceiveDataNotifier
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view.EnterAuthCodeFragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class AuthorizationViewControllersFactoryImp(
    private val enteredCorrectPhoneNumberNotifier: IReceiveDataNotifier<String>,
    private val enteredCorrectAuthCodeNotifier: IReceiveDataNotifier<String>,
    private val backPressedInPhoneNumberScreenNotifier: IReceiveDataNotifier<Unit>,
    private val backPressedInAuthCodeScreenNotifier: IReceiveDataNotifier<Unit>
) : AuthorizationViewControllersFactory {

    override fun createEnterPhoneNumberViewController(
        tdLibGateway: TDLibGateway
    ): SupportAppScreenAndIReceiveDataNotifierPair {
        val enterPhoneNumberAppScreen = object : SupportAppScreen() {
            override fun getFragment(): Fragment = EnterPhoneNumberFragment.createNewInstance() as Fragment
        }

        return Triple(
            enterPhoneNumberAppScreen,
            enteredCorrectPhoneNumberNotifier,
            backPressedInPhoneNumberScreenNotifier
        )
    }

    override fun createEnterAuthCodeViewController(tdLibGateway: TDLibGateway): SupportAppScreenAndIReceiveDataNotifierPair {
        val enterAuthCodeAppScreen = object : SupportAppScreen() {
            override fun getFragment(): Fragment = EnterAuthCodeFragment.createNewInstance() as Fragment
        }

        return Triple(
            enterAuthCodeAppScreen,
            enteredCorrectAuthCodeNotifier,
            backPressedInAuthCodeScreenNotifier
        )
    }

}