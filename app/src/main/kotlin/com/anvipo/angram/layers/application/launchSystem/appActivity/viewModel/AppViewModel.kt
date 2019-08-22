package com.anvipo.angram.layers.application.launchSystem.appActivity.viewModel

import com.anvipo.angram.layers.application.launchSystem.appActivity.types.SetNavigatorEventParameters
import com.anvipo.angram.layers.core.NetworkConnectionState
import com.anvipo.angram.layers.core.base.interfaces.BaseViewModel
import com.anvipo.angram.layers.core.events.SingleLiveEvent

interface AppViewModel : BaseViewModel {

    val setNavigatorEvents: SingleLiveEvent<SetNavigatorEventParameters>

    fun onResumeFragments()
    fun onChangeNetworkConnectionState(newState: NetworkConnectionState)

}