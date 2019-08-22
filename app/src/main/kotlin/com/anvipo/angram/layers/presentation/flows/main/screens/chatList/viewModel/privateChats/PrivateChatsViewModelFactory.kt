package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel.privateChats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.koin.core.KoinComponent
import org.koin.core.get

object PrivateChatsViewModelFactory :
    ViewModelProvider.Factory,
    KoinComponent {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        get<PrivateChatsViewModel>() as T

}
