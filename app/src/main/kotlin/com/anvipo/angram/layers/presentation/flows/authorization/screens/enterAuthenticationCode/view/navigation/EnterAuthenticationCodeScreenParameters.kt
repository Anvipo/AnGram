package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.view.navigation

class EnterAuthenticationCodeScreenParameters(
    val shouldShowBackButton: Boolean,
    val expectedCodeLength: Int,
    val enteredPhoneNumber: String,
    val registrationRequired: Boolean,
    val termsOfServiceText: String
)