package com.anvipo.angram.applicationLayer.di

import com.anvipo.angram.coreLayer.ResourceManager
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

object SystemInfrastructureModule {

    private val ciceroneQualifier = named("cicerone")
    internal val routerQualifier = named("router")
    internal val navigatorHolderQualifier = named("navigatorHolder")
    internal val resourceManagerQualifier = named("resourceManager")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<Cicerone<Router>>(ciceroneQualifier) {
            Cicerone.create()
        }

        single<Router>(routerQualifier) {
            get<Cicerone<Router>>(ciceroneQualifier).router
        }

        single<NavigatorHolder>(navigatorHolderQualifier) {
            get<Cicerone<Router>>(ciceroneQualifier).navigatorHolder
        }

        single<ResourceManager>(resourceManagerQualifier) {
            ResourceManager(
                context = get()
            )
        }

    }

}