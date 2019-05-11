package com.anvipo.angram.layers.presentation.flows.main.coordinator

import com.anvipo.angram.layers.core.base.classes.BaseCoordinatorImpl
import com.anvipo.angram.layers.global.types.SystemMessageSendChannel
import com.anvipo.angram.layers.presentation.flows.main.coordinator.interfaces.MainCoordinator
import com.anvipo.angram.layers.presentation.flows.main.coordinator.interfaces.MainCoordinatorRouteEventHandler
import com.anvipo.angram.layers.presentation.flows.main.coordinator.screensFactory.main.MainScreensFactory
import com.anvipo.angram.layers.presentation.flows.main.coordinator.types.MainFlowCoordinateResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.terrakok.cicerone.Router
import kotlin.coroutines.suspendCoroutine

class MainCoordinatorImpl(
    private val router: Router,
    private val screensFactory: MainScreensFactory,
    systemMessageSendChannel: SystemMessageSendChannel
) :
    BaseCoordinatorImpl<MainFlowCoordinateResult>(
        systemMessageSendChannel
    ),
    MainCoordinator,
    MainCoordinatorRouteEventHandler {

    override suspend fun start(): MainFlowCoordinateResult = showChatListScreen()

    override suspend fun onPressedBackButtonInChatListScreen() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        withContext(Dispatchers.Main) {
            router.exit()
        }
    }


    private suspend fun showChatListScreen(): MainFlowCoordinateResult = suspendCoroutine {
        finishFlowContinuation = it

        myLaunch {
            showChatListHelper()
        }
    }

    private suspend fun showChatListHelper() {
        myLog(invokationPlace = object {}.javaClass.enclosingMethod!!.name)

        val chatListScreen = withContext(Dispatchers.Default) {
            screensFactory
                .chatListScreenFactory
                .createChatListScreen()
        }

        withContext(Dispatchers.Main) {
            router.newRootScreen(chatListScreen)
        }
    }

}