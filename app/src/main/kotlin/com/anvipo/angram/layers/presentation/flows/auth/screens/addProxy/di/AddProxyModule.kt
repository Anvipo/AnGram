package com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.di

import androidx.fragment.app.Fragment
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.layers.businessLogic.di.UseCasesModule.addProxyUseCaseQualifier
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.addProxy.AddProxyScreenFactory
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.screensFactory.addProxy.AddProxyScreenFactoryImp
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.presenter.AddProxyPresenter
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.presenter.AddProxyPresenterImp
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.types.ProxyType
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.AddProxyFragment
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.view.AddProxyView
import org.koin.core.context.GlobalContext
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.android.support.SupportAppScreen

object AddProxyModule {

    class AddProxyScreen(
        private val parameters: AddProxyScreenParameters
    ) : SupportAppScreen() {
        override fun getFragment(): Fragment =
            GlobalContext.get().koin.get(addProxyScreenCodeViewQualifier) {
                parametersOf(parameters)
            }
    }

    class AddProxyScreenParameters(
        val shouldShowBackButton: Boolean,
        val proxyType: ProxyType
    )

    val addProxyScreenFactoryQualifier: StringQualifier = named("addProxyScreenFactory")

    val addProxyScreenCodeViewQualifier: StringQualifier = named("addProxyScreenCodeView")
    val addProxyScreenCodeScreenQualifier: StringQualifier = named("addProxyScreenCodeScreen")

    val addProxyPresenterQualifier: StringQualifier = named("addProxyPresenter")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        factory<AddProxyScreenFactory>(
            addProxyScreenFactoryQualifier
        ) {
            AddProxyScreenFactoryImp(
                koinScope = this
            )
        }

        factory<AddProxyView>(
            addProxyScreenCodeViewQualifier
        ) { (parameters: AddProxyScreenParameters) ->
            AddProxyFragment.createNewInstance(
                shouldShowBackButton = parameters.shouldShowBackButton,
                proxyType = parameters.proxyType
            )
        }

        factory<SupportAppScreen>(
            addProxyScreenCodeScreenQualifier
        ) { (parameters: AddProxyScreenParameters) ->
            AddProxyScreen(parameters = parameters)
        }

        factory<AddProxyPresenter>(addProxyPresenterQualifier) {
            AddProxyPresenterImp(
                routeEventHandler = get(authorizationCoordinatorQualifier),
                useCase = get(addProxyUseCaseQualifier),
                resourceManager = get(resourceManagerQualifier)
            )
        }

    }

}