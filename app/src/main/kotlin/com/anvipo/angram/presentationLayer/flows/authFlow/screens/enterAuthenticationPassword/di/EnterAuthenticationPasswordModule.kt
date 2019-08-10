package com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationPassword.di

import androidx.fragment.app.Fragment
import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.businessLogicLayer.di.UseCasesModule.enterAuthenticationPasswordUseCaseQualifier
import com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterAuthenticationPasswordUseCase.EnterAuthenticationPasswordUseCase
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationPasswordRouteEventHandler
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.enterPasswordScreenFactory.EnterAuthenticationPasswordScreenFactory
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.enterPasswordScreenFactory.EnterAuthenticationPasswordScreenFactoryImp
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationPassword.presenter.EnterAuthenticationPasswordPresenter
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationPassword.presenter.EnterAuthenticationPasswordPresenterImp
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationPassword.view.EnterAuthenticationPasswordFragment
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationPassword.view.EnterAuthenticationPasswordView
import org.koin.core.context.GlobalContext
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.android.support.SupportAppScreen

object EnterAuthenticationPasswordModule {

    class EnterPasswordScreen : SupportAppScreen() {
        override fun getFragment(): Fragment =
            GlobalContext.get().koin.get(enterAuthenticationPasswordViewQualifier)
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
            EnterAuthenticationPasswordScreenFactoryImp(
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
            EnterAuthenticationPasswordPresenterImp(
                routeEventHandler = get<AuthorizationCoordinatorEnterAuthenticationPasswordRouteEventHandler>(
                    authorizationCoordinatorQualifier
                ),
                useCase = get<EnterAuthenticationPasswordUseCase>(enterAuthenticationPasswordUseCaseQualifier),
                resourceManager = get<ResourceManager>(resourceManagerQualifier)
            )
        }

    }

}