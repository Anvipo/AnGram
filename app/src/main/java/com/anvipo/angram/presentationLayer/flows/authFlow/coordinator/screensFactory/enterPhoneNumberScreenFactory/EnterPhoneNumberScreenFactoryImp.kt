package com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.enterPhoneNumberScreenFactory

import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterPhoneNumber.di.EnterPhoneNumberModule.enterPhoneNumberScreenQualifier
import org.koin.core.scope.Scope
import ru.terrakok.cicerone.android.support.SupportAppScreen

class EnterPhoneNumberScreenFactoryImp(
    private val koinScope: Scope
) : EnterPhoneNumberScreenFactory {

    override fun createEnterPhoneNumberScreen(): SupportAppScreen =
        koinScope.get(enterPhoneNumberScreenQualifier)

}