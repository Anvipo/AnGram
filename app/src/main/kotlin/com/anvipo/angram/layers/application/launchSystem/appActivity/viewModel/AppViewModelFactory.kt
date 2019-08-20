package com.anvipo.angram.layers.application.launchSystem.appActivity.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.koin.core.KoinComponent
import org.koin.core.get

object AppViewModelFactory : ViewModelProvider.Factory, KoinComponent {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return get<AppViewModel>() as T
    }

}