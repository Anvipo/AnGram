package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPhoneNumber.presenter

import com.anvipo.angram.layers.core.base.interfaces.BasePresenter

interface EnterAuthenticationPhoneNumberPresenter : BasePresenter {

    fun onAddProxyButtonPressed()
    fun onItemClicked(index: Int)

    fun onNextButtonPressed(enteredPhoneNumber: String)
    fun onPhoneNumberTextChanged(text: String)

}