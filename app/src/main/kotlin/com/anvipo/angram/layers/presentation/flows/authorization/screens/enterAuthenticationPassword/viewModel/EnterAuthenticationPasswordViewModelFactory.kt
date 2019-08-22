package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
import org.koin.core.KoinComponent

object EnterAuthenticationPasswordViewModelFactory :
    ViewModelProvider.Factory,
    KoinComponent {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        authorizationCoordinatorScope!!.get<EnterAuthenticationPasswordViewModel>() as T

}