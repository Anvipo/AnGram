package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory

import androidx.fragment.app.Fragment
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.businessLogicLayer.useCases.enterAuthCodeUseCase.EnterAuthCodeUseCase
import com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase.EnterPhoneNumberUseCase
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.presenter.EnterAuthCodePresenterImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view.EnterAuthCodeFragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view.EnterAuthCodeView
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenterImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberFragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberView
import ru.terrakok.cicerone.android.support.SupportAppScreen
import java.lang.ref.WeakReference

class AuthorizationViewControllersFactoryImp(
    private val enterPhoneNumberUseCase: EnterPhoneNumberUseCase,
    private val enterAuthCodeUseCase: EnterAuthCodeUseCase
) : AuthorizationViewControllersFactory {

    override fun createEnterPhoneNumberViewController(tdLibGateway: TDLibGateway): Pair<EnterPhoneNumberView, SupportAppScreen> {
        val enterPhoneNumberAppScreen = object : SupportAppScreen() {
            override fun getFragment(): Fragment = EnterPhoneNumberFragment.createNewInstance() as Fragment
        }

        val enterPhoneNumberView = enterPhoneNumberAppScreen.fragment as EnterPhoneNumberView

        // TODO: apply DI

        enterPhoneNumberView.presenter =
            EnterPhoneNumberPresenterImp(
                WeakReference(enterPhoneNumberView),
                enterPhoneNumberUseCase
            )

        return enterPhoneNumberView to enterPhoneNumberAppScreen
    }

    override fun createEnterAuthCodeViewController(tdLibGateway: TDLibGateway): Pair<EnterAuthCodeView, SupportAppScreen> {
        val enterAuthCodeAppScreen = object : SupportAppScreen() {
            override fun getFragment(): Fragment = EnterAuthCodeFragment.createNewInstance() as Fragment
        }

        val enterAuthCodeView = enterAuthCodeAppScreen.fragment as EnterAuthCodeView

        // TODO: apply DI

        enterAuthCodeView.presenter =
            EnterAuthCodePresenterImp(
                WeakReference(enterAuthCodeView),
                enterAuthCodeUseCase
            )

        return enterAuthCodeView to enterAuthCodeAppScreen

    }

}