package com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.presenter

import com.anvipo.angram.layers.core.base.interfaces.BasePresenter
import org.drinkless.td.libcore.telegram.TdApi

interface AddProxyPresenter : BasePresenter {

    fun addProxyButtonTapped()

    fun setProxyType(proxyType: TdApi.ProxyType)

    fun onServerTextChanged(serverText: CharSequence?)
    fun onPortTextChanged(portText: CharSequence?)
    fun onSecretTextChanged(secretText: CharSequence?)

    fun messageDialogPositiveClicked(tag: String)

}