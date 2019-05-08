package com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator

import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinator
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.screensFactory.MainScreensFactory
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import kotlin.coroutines.CoroutineContext

class MainCoordinatorImp(
    private val router: Router,
    private val viewControllersFactory: MainScreensFactory
) : BaseCoordinator(), MainCoordinator {
    override fun cancelAllJobs() {
        TODO("not implemented")
    }

    override val coroutineContext: CoroutineContext
        get() = TODO("not implemented")

    override var finishFlow: (() -> Unit)? = null

    override fun coldStart() {
        showMainScreen()
    }


    /// PRIVATE


    private fun showMainScreen() {
        val mainScreen = viewControllersFactory.createMainScreen()

        router.newRootScreen(mainScreen as Screen)
    }

}