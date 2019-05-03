package com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator

import com.anvipo.angram.applicationLayer.navigation.router.Routable
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinator
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.screensFactory.MainScreensFactory

class MainCoordinatorImp(
    private val router: Routable,
    private val screensFactory: MainScreensFactory
) : BaseCoordinator(), MainCoordinator {

    override var finishFlow: (() -> Unit)? = null

    override fun start() {
        showMainScreen()
    }


    /// PRIVATE


    private fun showMainScreen() {
        val mainScreen = screensFactory.createMainScreen()

        router.setRootViewController(viewController = mainScreen)
    }

}