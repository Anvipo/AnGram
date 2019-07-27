package com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator

import com.anvipo.angram.presentationLayer.common.baseClasses.BaseCoordinatorImp
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.interfaces.MainCoordinator
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.interfaces.MainCoordinatorRouteEventHandler
import com.anvipo.angram.presentationLayer.userStories.mainUserStory.coordinator.screensFactory.MainScreensFactory
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import kotlin.coroutines.CoroutineContext

class MainCoordinatorImp(
    private val router: Router,
    private val screensFactory: MainScreensFactory
) :
    BaseCoordinatorImp(),
    MainCoordinator,
    MainCoordinatorRouteEventHandler {

    override fun cancelAllJobs() {
        TODO("not implemented")
    }

    override val coroutineContext: CoroutineContext
        get() = TODO("not implemented")

    override var finishFlow: (() -> Unit)? = null

    override fun coldStart() {
        TODO()

        showMainScreen()
    }


    /// PRIVATE


    private fun showMainScreen() {
        val mainScreen = screensFactory.createMainScreen()

        router.newRootScreen(mainScreen as Screen)
    }

}