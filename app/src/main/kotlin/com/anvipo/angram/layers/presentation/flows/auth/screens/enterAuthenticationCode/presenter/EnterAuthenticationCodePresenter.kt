package com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationCode.presenter

import com.anvipo.angram.layers.presentation.common.interfaces.BasePresenter
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType

interface EnterAuthenticationCodePresenter : BasePresenter {

    fun onNextButtonPressed(
        enteredAuthenticationCode: CorrectAuthenticationCodeType,
        lastName: String,
        firstName: String
    )

    fun onResendAuthenticationCodeButtonPressed()

    @ExperimentalUnsignedTypes
    fun onGetExpectedCodeLength(expectedCodeLength: UInt)

    fun onGetEnteredPhoneNumber(enteredPhoneNumber: String)
    fun onGetRegistrationRequired(registrationRequired: Boolean)
    fun onGetTermsOfServiceText(termsOfServiceText: String)

    fun onAuthenticationCodeTextChanged(text: CharSequence?)
    fun onFirstNameTextChanged(text: CharSequence?)
    fun onLastNameTextChanged(text: CharSequence?)

}