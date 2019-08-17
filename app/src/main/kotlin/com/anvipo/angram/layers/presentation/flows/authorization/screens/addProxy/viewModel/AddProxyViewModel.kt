package com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.viewModel

import com.anvipo.angram.layers.core.base.interfaces.BaseViewModel
import com.anvipo.angram.layers.core.events.ShowViewEvent
import com.anvipo.angram.layers.core.events.SingleLiveEvent
import org.drinkless.td.libcore.telegram.TdApi

interface AddProxyViewModel : BaseViewModel {

    val showAddProxyEvents: SingleLiveEvent<ShowViewEvent>

    fun addProxyButtonTapped()

    fun setProxyType(proxyType: TdApi.ProxyType)

    fun onServerTextChanged(serverText: CharSequence?)
    fun onPortTextChanged(portText: CharSequence?)
    fun onSecretTextChanged(secretText: CharSequence?)

    fun messageDialogPositiveClicked(tag: String)

}