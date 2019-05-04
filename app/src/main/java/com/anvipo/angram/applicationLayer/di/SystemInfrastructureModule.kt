package com.anvipo.angram.applicationLayer.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

object SystemInfrastructureModule {

    val module: Module = module {

        single<Cicerone<Router>> {
            Cicerone.create()
        }

        single<Router> {
            get<Cicerone<Router>>().router
        }

        single<NavigatorHolder> {
            get<Cicerone<Router>>().navigatorHolder
        }

    }

}