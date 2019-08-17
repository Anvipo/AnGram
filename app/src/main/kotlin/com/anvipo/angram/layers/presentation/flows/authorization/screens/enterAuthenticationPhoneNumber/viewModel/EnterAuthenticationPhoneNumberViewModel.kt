package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.viewModel

import com.anvipo.angram.layers.core.base.interfaces.BaseViewModel
import com.anvipo.angram.layers.core.events.EnableViewEventsParameters
import com.anvipo.angram.layers.core.events.ShowViewEventParameters
import com.anvipo.angram.layers.core.events.SingleLiveEvent

interface EnterAuthenticationPhoneNumberViewModel : BaseViewModel {

    val showNextButtonEvents: SingleLiveEvent<ShowViewEventParameters>
    val enableNextButtonEvents: SingleLiveEvent<EnableViewEventsParameters>

    fun onAddProxyButtonPressed()
    fun onItemClicked(index: Int)

    fun onNextButtonPressed(enteredPhoneNumber: String)
    fun onPhoneNumberTextChanged(text: String)

}