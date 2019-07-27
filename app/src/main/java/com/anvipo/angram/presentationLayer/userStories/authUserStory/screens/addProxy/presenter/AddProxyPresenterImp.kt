package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.addProxy.presenter

import com.anvipo.angram.presentationLayer.common.baseClasses.BasePresenterImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorAddProxyRouteEventHandler
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.addProxy.view.AddProxyView
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class AddProxyPresenterImp(
    private val routeEventHandler: AuthorizationCoordinatorAddProxyRouteEventHandler
) : BasePresenterImp<AddProxyView>(), AddProxyPresenter {

    override fun onBackPressed() {
        routeEventHandler.onPressedBackButtonInAddProxyScreen()
    }

}