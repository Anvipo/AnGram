package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
import org.koin.core.KoinComponent

object EnterAuthenticationCodeViewModelFactory :
    ViewModelProvider.Factory,
    KoinComponent {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        authorizationCoordinatorScope!!.get<EnterAuthenticationCodeViewModel>() as T

}