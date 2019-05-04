package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory

import com.anvipo.angram.businessLogicLayer.assemblies.UseCasesAssembly
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.presenter.EnterAuthCodePresenterImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view.EnterAuthCodeFragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.view.EnterAuthCodeView
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenterImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberFragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberView
import java.lang.ref.WeakReference

object AuthorizationViewControllersFactoryImp : AuthorizationViewControllersFactory {

    override fun createEnterPhoneNumberViewController(tdLibGateway: TDLibGateway): EnterPhoneNumberView {
        val enterPhoneNumberView = EnterPhoneNumberFragment.createNewInstance()

        val useCase = UseCasesAssembly.enterPhoneNumberUseCase

        enterPhoneNumberView.presenter =
            EnterPhoneNumberPresenterImp(
                WeakReference(enterPhoneNumberView),
                useCase
            )

        return enterPhoneNumberView
    }

    override fun createEnterAuthCodeViewController(tdLibGateway: TDLibGateway): EnterAuthCodeView {
        val enterAuthCodeView = EnterAuthCodeFragment.createNewInstance()

        val useCase = UseCasesAssembly.enterAuthCodeUseCase

        enterAuthCodeView.presenter =
            EnterAuthCodePresenterImp(
                WeakReference(enterAuthCodeView),
                useCase
            )

        return enterAuthCodeView

    }

}