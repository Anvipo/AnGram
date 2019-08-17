package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.viewModel

import androidx.lifecycle.LiveData
import com.anvipo.angram.layers.core.base.interfaces.BaseViewModel
import com.anvipo.angram.layers.core.events.SingleLiveEvent
import com.anvipo.angram.layers.core.events.parameters.EnableViewEventsParameters
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters

interface EnterAuthenticationPhoneNumberViewModel : BaseViewModel {

    val showNextButtonEvents: LiveData<ShowViewEventParameters>
    val savedEnterPhoneNumberEvents: LiveData<String?>
    val enableNextButtonEvents: LiveData<EnableViewEventsParameters>

    fun onAddProxyButtonPressed()
    fun onItemClicked(index: Int)

    fun onNextButtonPressed(enteredPhoneNumber: String)
    fun onPhoneNumberTextChanged(text: String)

}