package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.viewModel

import androidx.lifecycle.LiveData
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters
import com.anvipo.angram.layers.presentation.flows.authorization.screens.base.viewModel.BaseAuthorizationFlowViewModel
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.types.EnterAuthenticationCodeScreenSavedInputData
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.types.SetExpectedCodeLengthEventParameters

interface EnterAuthenticationCodeViewModel : BaseAuthorizationFlowViewModel {

    val setExpectedCodeLengthEvents: LiveData<SetExpectedCodeLengthEventParameters>
    val showRegistrationViewsEvents: LiveData<ShowViewEventParameters>
    val enterAuthenticationCodeScreenSavedInputData: LiveData<EnterAuthenticationCodeScreenSavedInputData>

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