package com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view.navigation

import androidx.fragment.app.Fragment
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view.AddProxyFragment
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.parameter.parametersOf
import ru.terrakok.cicerone.android.support.SupportAppScreen

class AddProxyScreen(
    private val parameters: AddProxyScreenParameters
) : SupportAppScreen(), KoinComponent {

    override fun getFragment(): Fragment =
        get<AddProxyFragment> { parametersOf(parameters) }

}