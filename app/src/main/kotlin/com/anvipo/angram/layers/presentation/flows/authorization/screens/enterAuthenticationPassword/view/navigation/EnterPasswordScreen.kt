package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.view.navigation

import androidx.fragment.app.Fragment
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationPassword.view.EnterAuthenticationPasswordFragment
import org.koin.core.KoinComponent
import ru.terrakok.cicerone.android.support.SupportAppScreen

class EnterPasswordScreen : SupportAppScreen(), KoinComponent {

    override fun getFragment(): Fragment =
        authorizationCoordinatorScope!!.get<EnterAuthenticationPasswordFragment>()

}