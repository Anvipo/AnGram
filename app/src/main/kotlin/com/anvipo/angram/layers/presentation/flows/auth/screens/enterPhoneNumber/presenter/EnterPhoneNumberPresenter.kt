package com.anvipo.angram.layers.presentation.flows.auth.screens.enterPhoneNumber.presenter

import com.anvipo.angram.layers.presentation.common.interfaces.BasePresenter

interface EnterPhoneNumberPresenter : BasePresenter {

    fun onAddProxyButtonPressed()
    fun onItemClicked(index: Int)

    fun onNextButtonPressed(enteredPhoneNumber: String)
    fun onPhoneNumberTextChanged(text: String)

}