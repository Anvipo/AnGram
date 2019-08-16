package com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.di

import androidx.fragment.app.Fragment
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.resourceManagerQualifier
import com.anvipo.angram.layers.businessLogic.di.UseCasesModule.addProxyUseCaseQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.di.AuthorizationCoordinatorModule.authorizationCoordinatorQualifier
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.addProxy.AddProxyScreenFactory
import com.anvipo.angram.layers.presentation.flows.authorization.coordinator.screensFactory.addProxy.AddProxyScreenFactoryImpl
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.viewModel.AddProxyViewModel
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.viewModel.AddProxyViewModelImpl
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.types.ProxyType
import com.anvipo.angram.layers.presentation.flows.authorization.screens.addProxy.view.AddProxyFragment
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.android.support.SupportAppScreen

object AddProxyModule {

    class AddProxyScreen(
        private val parameters: AddProxyScreenParameters
    ) : SupportAppScreen(), KoinComponent {
        override fun getFragment(): Fragment =
            get(addProxyViewQualifier) {
                parametersOf(parameters)
            }
    }

    class AddProxyScreenParameters(
        val shouldShowBackButton: Boolean,
        val proxyType: ProxyType
    )

    val addProxyScreenFactoryQualifier: StringQualifier = named("addProxyScreenFactory")

    val addProxyViewQualifier: StringQualifier = named("addProxyView")
    val addProxyScreenQualifier: StringQualifier = named("addProxyScreen")

    val addProxyViewModelQualifier = named("addProxyViewModel")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        factory<AddProxyScreenFactory>(
            addProxyScreenFactoryQualifier
        ) {
            AddProxyScreenFactoryImpl(
                koinScope = this
            )
        }

        factory<AddProxyFragment>(
            addProxyViewQualifier
        ) { (parameters: AddProxyScreenParameters) ->
            AddProxyFragment.createNewInstance(
                shouldShowBackButton = parameters.shouldShowBackButton,
                proxyType = parameters.proxyType
            )
        }

        factory<SupportAppScreen>(
            addProxyScreenQualifier
        ) { (parameters: AddProxyScreenParameters) ->
            AddProxyScreen(parameters = parameters)
        }

        factory<AddProxyViewModel>(addProxyViewModelQualifier) {
            AddProxyViewModelImpl(
                routeEventHandler = get(authorizationCoordinatorQualifier),
                useCase = get(addProxyUseCaseQualifier),
                resourceManager = get(resourceManagerQualifier)
            )
        }

    }

}