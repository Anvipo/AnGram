package com.anvipo.angram.layers.application.launchSystem.appActivity.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anvipo.angram.layers.application.launchSystem.appActivity.di.AppActivityModule
import org.koin.core.KoinComponent
import org.koin.core.get

object AppViewModelFactory : ViewModelProvider.Factory, KoinComponent {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return get<AppViewModel>() as T
    }

}