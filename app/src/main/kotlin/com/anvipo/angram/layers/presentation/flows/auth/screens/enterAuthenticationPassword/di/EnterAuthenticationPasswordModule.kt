package com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationPassword.di

import androidx.fragment.app.Fragment
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.layers.businessLogic.di.UseCasesModule.enterAuthenticationPasswordUseCaseQualifier
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.enterAuthenticationPassword.EnterAuthenticationPasswordScreenFactory
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.enterAuthenticationPassword.EnterAuthenticationPasswordScreenFactoryImpl
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationPassword.presenter.EnterAuthenticationPasswordPresenter
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationPassword.presenter.EnterAuthenticationPasswordPresenterImpl
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationPassword.view.EnterAuthenticationPasswordFragment
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationPassword.view.EnterAuthenticationPasswordView
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.android.support.SupportAppScreen

object EnterAuthenticationPasswordModule {

    class EnterPasswordScreen : SupportAppScreen(), KoinComponent {
        override fun getFragment(): Fragment = get(enterAuthenticationPasswordViewQualifier)
    }

    val enterAuthenticationPasswordScreenFactoryQualifier: StringQualifier =
        named("enterAuthenticationPasswordScreenFactory")

    val enterAuthenticationPasswordViewQualifier: StringQualifier =
        named("enterAuthenticationPasswordView")
    val enterAuthenticationPasswordScreenQualifier: StringQualifier =
        named("enterAuthenticationPasswordScreen")

    val enterAuthenticationPasswordPresenterQualifier: StringQualifier = named("enterAuthenticationPasswordPresenter")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        factory<EnterAuthenticationPasswordScreenFactory>(enterAuthenticationPasswordScreenFactoryQualifier) {
            EnterAuthenticationPasswordScreenFactoryImpl(
                koinScope = this
            )
        }

        factory<EnterAuthenticationPasswordView>(
            enterAuthenticationPasswordViewQualifier
        ) {
            EnterAuthenticationPasswordFragment.createNewInstance()
        }

        factory<SupportAppScreen>(
            enterAuthenticationPasswordScreenQualifier
        ) {
            EnterPasswordScreen()
        }

        factory<EnterAuthenticationPasswordPresenter>(enterAuthenticationPasswordPresenterQualifier) {
            EnterAuthenticationPasswordPresenterImpl(
                routeEventHandler = get(authorizationCoordinatorQualifier),
                useCase = get(enterAuthenticationPasswordUseCaseQualifier),
                resourceManager = get(resourceManagerQualifier)
            )
        }

    }

}