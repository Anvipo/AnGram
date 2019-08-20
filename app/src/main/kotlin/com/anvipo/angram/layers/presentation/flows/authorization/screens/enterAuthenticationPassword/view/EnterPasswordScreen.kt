package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.view

import androidx.fragment.app.Fragment
import org.koin.core.KoinComponent
import org.koin.core.get
import ru.terrakok.cicerone.android.support.SupportAppScreen

class EnterPasswordScreen :
    SupportAppScreen(),
    KoinComponent {

    override fun getFragment(): Fragment = get<EnterAuthenticationPasswordFragment>()

}