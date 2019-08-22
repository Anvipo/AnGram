package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.di

import com.anvipo.angram.layers.presentation.flows.main.coordinator.di.MainCoordinatorModule.mainCoordinatorQualifier
import com.anvipo.angram.layers.presentation.flows.main.coordinator.di.MainCoordinatorModule.mainCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.main.coordinator.screensFactory.chatList.ChatListScreenFactory
import com.anvipo.angram.layers.presentation.flows.main.coordinator.screensFactory.chatList.ChatListScreenFactoryImpl
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.chatList.ChatListFragment
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.chatList.navigation.ChatListScreen
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.chatList.ChatListViewModel
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.chatList.ChatListViewModelFactory
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.chatList.ChatListViewModelImpl
import org.koin.core.module.Module
import org.koin.dsl.module

object ChatListModule {

    val module: Module = module {

        factory<ChatListScreenFactory> {
            ChatListScreenFactoryImpl(
                koinScope = this
            )
        }

        factory {
            ChatListFragment.createNewInstance()
        }

        factory {
            ChatListScreen()
        }

        single {
            ChatListViewModelFactory
        }

        factory<ChatListViewModel> {
            ChatListViewModelImpl(
                routeEventHandler = mainCoordinatorScope.get(mainCoordinatorQualifier),
                useCase = mainCoordinatorScope.get(),
                resourceManager = get()
            )
        }

    }

}
