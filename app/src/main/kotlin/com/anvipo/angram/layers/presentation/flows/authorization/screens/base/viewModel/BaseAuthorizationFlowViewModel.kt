package com.anvipo.angram.layers.presentation.flows.authorization.screens.base.viewModel

import androidx.lifecycle.LiveData
import com.anvipo.angram.layers.core.base.interfaces.BaseViewModel
import com.anvipo.angram.layers.core.events.parameters.EnableViewEventsParameters
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters

interface BaseAuthorizationFlowViewModel : BaseViewModel {

    val showNextButtonEvents: LiveData<ShowViewEventParameters>
    val enableNextButtonEvents: LiveData<EnableViewEventsParameters>

}