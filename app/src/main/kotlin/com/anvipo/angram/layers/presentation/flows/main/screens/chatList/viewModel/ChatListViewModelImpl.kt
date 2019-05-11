package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel

import com.anvipo.angram.layers.businessLogic.useCases.flows.main.chatList.ChatListUseCase
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.core.base.classes.BaseViewModelImpl
import com.anvipo.angram.layers.presentation.flows.main.coordinator.interfaces.MainCoordinatorRouteEventHandler

class ChatListViewModelImpl(
    private val routeEventHandler: MainCoordinatorRouteEventHandler,
    private val useCase: ChatListUseCase,
    private val resourceManager: ResourceManager
) : BaseViewModelImpl(), ChatListViewModel {

    override fun onBackPressed() {
        myLaunch {
            routeEventHandler.onPressedBackButtonInChatListScreen()
        }
    }

}