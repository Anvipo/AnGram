package com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.presenter

import com.anvipo.angram.presentationLayer.common.interfaces.BasePresenter
import org.drinkless.td.libcore.telegram.TdApi

interface AddProxyPresenter : BasePresenter {

    fun addProxyButtonTapped()

    fun setProxyType(proxyType: TdApi.ProxyType)

    fun onServerTextChanged(serverText: CharSequence?)
    fun onPortTextChanged(portText: CharSequence?)
    fun onSecretTextChanged(secretText: CharSequence?)

    fun messageDialogPositiveClicked(tag: String)

}