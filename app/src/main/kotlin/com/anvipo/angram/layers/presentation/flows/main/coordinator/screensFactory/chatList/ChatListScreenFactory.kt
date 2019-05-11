package com.anvipo.angram.layers.presentation.flows.main.coordinator.screensFactory.chatList

import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.navigation.ChatListScreen

interface ChatListScreenFactory {

    fun createChatListScreen(): ChatListScreen

}