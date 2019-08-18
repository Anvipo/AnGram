package com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.viewModel

import androidx.lifecycle.LiveData
import com.anvipo.angram.layers.core.base.interfaces.BaseViewModel
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.types.AddProxyScreenSavedInputData
import org.drinkless.td.libcore.telegram.TdApi

interface AddProxyViewModel : BaseViewModel {

    val showAddProxyEvents: LiveData<ShowViewEventParameters>
    val addProxyScreenSavedInputDataEvents: LiveData<AddProxyScreenSavedInputData>
    val enteredServerAddress: LiveData<String?>
    @ExperimentalUnsignedTypes
    val enteredServerPort: LiveData<UInt?>
    val enteredAuthenticationKey: LiveData<String?>

    fun addProxyButtonTapped()

    fun setProxyType(proxyType: TdApi.ProxyType)

    fun onServerAddressChanged(serverText: CharSequence?)
    fun onServerPortChanged(portText: CharSequence?)
    fun onAuthenticationKeyChanged(secretText: CharSequence?)

    fun messageDialogPositiveClicked(tag: String?)

}