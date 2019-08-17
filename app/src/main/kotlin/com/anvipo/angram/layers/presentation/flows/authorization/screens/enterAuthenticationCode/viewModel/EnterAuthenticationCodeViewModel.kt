package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.viewModel

import com.anvipo.angram.layers.core.base.interfaces.BaseViewModel
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters
import com.anvipo.angram.layers.core.events.SingleLiveEvent
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.types.SetExpectedCodeLengthEventParameters

interface EnterAuthenticationCodeViewModel : BaseViewModel {

    val setExpectedCodeLengthEvents: SingleLiveEvent<SetExpectedCodeLengthEventParameters>
    val showNextButtonEvents: SingleLiveEvent<ShowViewEventParameters>
    val showRegistrationViewsEvents: SingleLiveEvent<ShowViewEventParameters>

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