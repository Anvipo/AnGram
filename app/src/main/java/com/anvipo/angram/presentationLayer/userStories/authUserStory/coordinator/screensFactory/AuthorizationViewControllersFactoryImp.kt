package com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.screensFactory

import com.anvipo.angram.businessLogicLayer.assemblies.UseCasesAssembly
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
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

}