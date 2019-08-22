package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.di

import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.chatList.navigation.PrivateChatsScreen
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.view.privateChats.PrivateChatsFragment
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.privateChats.PrivateChatsViewModel
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.privateChats.PrivateChatsViewModelFactory
import com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.privateChats.PrivateChatsViewModelImpl
import org.koin.core.module.Module
import org.koin.dsl.module

object PrivateChatsModule {

    val module: Module = module {

        factory {
            PrivateChatsFragment.createNewInstance()
        }

        factory {
            PrivateChatsScreen
        }

        single {
            PrivateChatsViewModelFactory
        }

        factory<PrivateChatsViewModel> {
            PrivateChatsViewModelImpl(
            )
        }

    }

}