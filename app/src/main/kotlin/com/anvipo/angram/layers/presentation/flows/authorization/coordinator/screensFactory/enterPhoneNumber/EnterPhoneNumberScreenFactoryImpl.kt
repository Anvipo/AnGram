package com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.enterPhoneNumber

import com.anvipo.angram.layers.presentation.flows.authorization.screens.enterPhoneNumber.di.EnterPhoneNumberModule.enterPhoneNumberScreenQualifier
import org.koin.core.scope.Scope
import ru.terrakok.cicerone.android.support.SupportAppScreen

class EnterPhoneNumberScreenFactoryImpl(
    private val koinScope: Scope
) : EnterPhoneNumberScreenFactory {

    override fun createEnterPhoneNumberScreen(): SupportAppScreen =
        koinScope.get(enterPhoneNumberScreenQualifier)

}