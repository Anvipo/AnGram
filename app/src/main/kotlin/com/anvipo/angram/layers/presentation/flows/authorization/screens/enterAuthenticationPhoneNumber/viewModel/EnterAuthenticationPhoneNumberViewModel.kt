package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.viewModel

import androidx.lifecycle.LiveData
import com.anvipo.angram.layers.presentation.flows.authorization.screens.base.viewModel.BaseAuthorizationFlowViewModel
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.types.EnterAuthenticationPhoneNumberScreenSavedInputData

interface EnterAuthenticationPhoneNumberViewModel : BaseAuthorizationFlowViewModel {

    val enterAuthenticationPhoneNumberScreenSavedInputDataEvents:
            LiveData<EnterAuthenticationPhoneNumberScreenSavedInputData>

    fun onAddProxyButtonPressed()
    fun onItemClicked(index: Int)

    fun onNextButtonPressed(enteredPhoneNumber: String)
    fun onPhoneNumberTextChanged(text: String)

}