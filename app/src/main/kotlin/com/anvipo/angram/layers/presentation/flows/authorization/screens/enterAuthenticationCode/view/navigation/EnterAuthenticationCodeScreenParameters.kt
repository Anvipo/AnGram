package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.view.navigation

class EnterAuthenticationCodeScreenParameters(
    val expectedCodeLength: Int,
    val enteredPhoneNumber: String,
    val registrationRequired: Boolean,
    val termsOfServiceText: String,
    var shouldShowBackButton: Boolean = true
)