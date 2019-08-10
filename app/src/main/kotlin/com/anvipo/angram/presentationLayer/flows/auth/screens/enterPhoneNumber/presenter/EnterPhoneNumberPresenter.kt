package com.anvipo.angram.presentationLayer.flows.auth.screens.enterPhoneNumber.presenter

import com.anvipo.angram.presentationLayer.common.interfaces.BasePresenter

interface EnterPhoneNumberPresenter : BasePresenter {

    fun onAddProxyButtonPressed()
    fun onItemClicked(index: Int)

    fun onNextButtonPressed(enteredPhoneNumber: String)
    fun onPhoneNumberTextChanged(text: String)

}