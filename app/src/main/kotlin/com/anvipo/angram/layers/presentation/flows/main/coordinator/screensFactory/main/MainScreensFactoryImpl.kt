package com.anvipo.angram.layers.presentation.flows.main.coordinator.screensFactory.main

import com.anvipo.angram.layers.presentation.flows.main.coordinator.screensFactory.chatList.ChatListScreenFactory

class MainScreensFactoryImpl(
    override val chatListScreenFactory: ChatListScreenFactory
) : MainScreensFactory