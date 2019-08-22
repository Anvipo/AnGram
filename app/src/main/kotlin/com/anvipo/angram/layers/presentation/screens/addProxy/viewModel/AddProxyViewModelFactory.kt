package com.anvipo.angram.layers.presentation.screens.addProxy.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.koin.core.KoinComponent
import org.koin.core.get

object AddProxyViewModelFactory :
    ViewModelProvider.Factory,
    KoinComponent {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return get<AddProxyViewModel>() as T
    }

}