package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.viewModel

import com.anvipo.angram.layers.core.base.interfaces.BaseViewModel
import com.anvipo.angram.layers.core.events.EnableViewEvents
import com.anvipo.angram.layers.core.events.ShowViewEvent
import com.anvipo.angram.layers.core.events.SingleLiveEvent

interface EnterAuthenticationPhoneNumberViewModel : BaseViewModel {

    val showNextButtonEvents: SingleLiveEvent<ShowViewEvent>
    val enableNextButtonEvents: SingleLiveEvent<EnableViewEvents>

    fun onAddProxyButtonPressed()
    fun onItemClicked(index: Int)

    fun onNextButtonPressed(enteredPhoneNumber: String)
    fun onPhoneNumberTextChanged(text: String)

}