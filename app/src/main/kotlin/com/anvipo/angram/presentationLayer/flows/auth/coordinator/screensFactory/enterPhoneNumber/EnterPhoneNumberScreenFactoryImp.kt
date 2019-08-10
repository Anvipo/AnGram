package com.anvipo.angram.presentationLayer.flows.auth.coordinator.screensFactory.enterPhoneNumber

import com.anvipo.angram.presentationLayer.flows.auth.screens.enterPhoneNumber.di.EnterPhoneNumberModule.enterPhoneNumberScreenQualifier
import org.koin.core.scope.Scope
import ru.terrakok.cicerone.android.support.SupportAppScreen

class EnterPhoneNumberScreenFactoryImp(
    private val koinScope: Scope
) : EnterPhoneNumberScreenFactory {

    override fun createEnterPhoneNumberScreen(): SupportAppScreen =
        koinScope.get(enterPhoneNumberScreenQualifier)

}