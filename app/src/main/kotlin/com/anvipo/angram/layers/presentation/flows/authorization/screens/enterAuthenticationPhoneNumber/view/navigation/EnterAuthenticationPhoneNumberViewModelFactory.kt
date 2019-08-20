package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.view.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.viewModel.EnterAuthenticationPhoneNumberViewModel
import org.koin.core.KoinComponent
import org.koin.core.get

object EnterAuthenticationPhoneNumberViewModelFactory :
    ViewModelProvider.Factory,
    KoinComponent {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        get<EnterAuthenticationPhoneNumberViewModel>() as T

}