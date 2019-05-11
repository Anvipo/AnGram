package com.anvipo.angram.layers.presentation.flows.main.screens.chatList.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.koin.core.KoinComponent
import org.koin.core.get

object ChatListViewModelFactory :
    ViewModelProvider.Factory,
    KoinComponent {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        get<ChatListViewModel>() as T

}