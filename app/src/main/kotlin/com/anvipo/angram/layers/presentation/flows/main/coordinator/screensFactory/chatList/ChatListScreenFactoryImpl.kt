package com.anvipo.angram.layers.presentation.flows.main.coordinator.screensFactory.chatList

import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.chatList.navigation.ChatListScreen
import org.koin.core.scope.Scope

class ChatListScreenFactoryImpl(
    private val koinScope: Scope
) : ChatListScreenFactory {

    override fun createChatListScreen(): ChatListScreen = koinScope.get()

}