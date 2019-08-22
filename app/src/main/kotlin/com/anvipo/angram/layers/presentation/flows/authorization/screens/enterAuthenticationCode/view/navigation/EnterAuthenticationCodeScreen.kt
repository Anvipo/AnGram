package com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.view.navigation

import androidx.fragment.app.Fragment
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorScope
import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterAuthenticationCode.view.EnterAuthenticationCodeFragment
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.parameter.parametersOf
import ru.terrakok.cicerone.android.support.SupportAppScreen

class EnterAuthenticationCodeScreen(
    private val parameters: EnterAuthenticationCodeScreenParameters
) : SupportAppScreen(), KoinComponent {

    override fun getFragment(): Fragment =
        authorizationCoordinatorScope!!.get<EnterAuthenticationCodeFragment> {
            parametersOf(parameters)
        }

}