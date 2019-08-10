package com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationCode.presenter

import com.anvipo.angram.presentationLayer.common.interfaces.BasePresenter
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType

@Suppress("EXPERIMENTAL_API_USAGE")
interface EnterAuthenticationCodePresenter : BasePresenter {

    fun onNextButtonPressed(
        enteredAuthenticationCode: CorrectAuthenticationCodeType,
        lastName: String,
        firstName: String
    )

    fun onResendAuthenticationCodeButtonPressed()

    fun onGetExpectedCodeLength(expectedCodeLength: UInt)
    fun onGetEnteredPhoneNumber(enteredPhoneNumber: String)
    fun onGetRegistrationRequired(registrationRequired: Boolean)
    fun onGetTermsOfServiceText(termsOfServiceText: String)

    fun onAuthenticationCodeTextChanged(text: CharSequence?)
    fun onFirstNameTextChanged(text: CharSequence?)
    fun onLastNameTextChanged(text: CharSequence?)

}