package com.anvipo.angram.applicationLayer.di

import com.anvipo.angram.coreLayer.ResourceManager
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

object SystemInfrastructureModule {

    private val ciceroneQualifier = named("cicerone")
    val routerQualifier: StringQualifier = named("router")
    val navigatorHolderQualifier: StringQualifier = named("navigatorHolder")
    val resourceManagerQualifier: StringQualifier = named("resourceManager")

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