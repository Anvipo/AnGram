package com.anvipo.angram.applicationLayer.di

import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

object SystemInfrastructureModule {

    private val cicerone: StringQualifier = named("cicerone")
    internal val router: StringQualifier = named("router")
    internal val navigatorHolder: StringQualifier = named("navigatorHolder")

    val module: Module = module {

        single<Cicerone<Router>>(cicerone) {
            Cicerone.create()
        }

        single<Router>(router) {
            get<Cicerone<Router>>(cicerone).router
        }

        single<NavigatorHolder>(navigatorHolder) {
            get<Cicerone<Router>>(cicerone).navigatorHolder
        }

    }

}