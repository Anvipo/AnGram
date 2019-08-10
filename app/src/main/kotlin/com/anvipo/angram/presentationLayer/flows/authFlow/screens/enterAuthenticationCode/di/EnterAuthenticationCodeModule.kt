package com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationCode.di

import androidx.fragment.app.Fragment
import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.businessLogicLayer.di.UseCasesModule.enterAuthenticationCodeUseCaseQualifier
import com.anvipo.angram.businessLogicLayer.useCases.authFlow.enterAuthenticationCode.EnterAuthenticationCodeUseCase
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.interfaces.AuthorizationCoordinatorEnterAuthenticationCodeRouteEventHandler
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.enterAuthenticationCode.EnterAuthenticationCodeScreenFactory
import com.anvipo.angram.presentationLayer.flows.authFlow.coordinator.screensFactory.enterAuthenticationCode.EnterAuthenticationCodeScreenFactoryImp
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationCode.presenter.EnterAuthenticationCodePresenter
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationCode.presenter.EnterAuthenticationCodePresenterImp
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationCode.view.EnterAuthenticationCodeFragment
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationCode.view.EnterAuthenticationCodeView
import org.koin.core.context.GlobalContext
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.android.support.SupportAppScreen

object EnterAuthenticationCodeModule {

    class EnterAuthenticationCodeScreen(
        private val parameters: EnterAuthCodeScreenParameters
    ) : SupportAppScreen() {
        override fun getFragment(): Fragment =
            GlobalContext.get().koin.get(enterAuthenticationCodeViewQualifier) {
                parametersOf(parameters)
            }
    }

    val enterAuthenticationCodeScreenFactoryQualifier: StringQualifier = named("enterAuthenticationCodeScreenFactory")

    val enterAuthenticationCodeViewQualifier: StringQualifier = named("enterAuthenticationCodeView")
    val enterAuthenticationCodeScreenQualifier: StringQualifier = named("enterAuthenticationCodeScreen")

    val enterAuthenticationCodePresenterQualifier: StringQualifier = named("enterAuthenticationCodePresenter")

    class EnterAuthCodeScreenParameters(
        val shouldShowBackButton: Boolean,
        val expectedCodeLength: Int,
        val enteredPhoneNumber: String,
        val registrationRequired: Boolean,
        val termsOfServiceText: String
    )

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        factory<EnterAuthenticationCodeScreenFactory>(
            enterAuthenticationCodeScreenFactoryQualifier
        ) {
            EnterAuthenticationCodeScreenFactoryImp(
                koinScope = this
            )
        }

        factory<EnterAuthenticationCodeView>(
            enterAuthenticationCodeViewQualifier
        ) { (parameters: EnterAuthCodeScreenParameters) ->
            EnterAuthenticationCodeFragment.createNewInstance(
                shouldShowBackButton = parameters.shouldShowBackButton,
                expectedCodeLength = parameters.expectedCodeLength,
                enteredPhoneNumber = parameters.enteredPhoneNumber,
                registrationRequired = parameters.registrationRequired,
                termsOfServiceText = parameters.termsOfServiceText
            )
        }

        factory<SupportAppScreen>(
            enterAuthenticationCodeScreenQualifier
        ) { (parameters: EnterAuthCodeScreenParameters) ->
            EnterAuthenticationCodeScreen(parameters = parameters)
        }

        factory<EnterAuthenticationCodePresenter>(enterAuthenticationCodePresenterQualifier) {
            EnterAuthenticationCodePresenterImp(
                routeEventHandler = get<AuthorizationCoordinatorEnterAuthenticationCodeRouteEventHandler>(
                    authorizationCoordinatorQualifier
                ),
                useCase = get<EnterAuthenticationCodeUseCase>(enterAuthenticationCodeUseCaseQualifier),
                resourceManager = get<ResourceManager>(resourceManagerQualifier)
            )
        }

    }

}