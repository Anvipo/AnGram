package com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.viewModel

import com.anvipo.angram.layers.core.base.interfaces.BaseViewModel
import com.anvipo.angram.layers.core.events.SingleLiveEvent
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters
import org.drinkless.td.libcore.telegram.TdApi

interface AddProxyViewModel : BaseViewModel {

    val showAddProxyEvents: SingleLiveEvent<ShowViewEventParameters>

    fun addProxyButtonTapped()

    fun setProxyType(proxyType: TdApi.ProxyType)

    fun onServerTextChanged(serverText: CharSequence?)
    fun onPortTextChanged(portText: CharSequence?)
    fun onSecretTextChanged(secretText: CharSequence?)

    fun messageDialogPositiveClicked(tag: String?)

}