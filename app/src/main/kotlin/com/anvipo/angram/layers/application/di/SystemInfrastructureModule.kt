package com.anvipo.angram.layers.application.di

import com.anvipo.angram.layers.core.ResourceManager
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

object SystemInfrastructureModule {

    val module: Module = module {

        single {
            Cicerone.create()
        }

        single {
            get<Cicerone<Router>>().router
        }

        single {
            get<Cicerone<Router>>().navigatorHolder
        }

        single {
            ResourceManager(
                context = androidApplication().applicationContext
            )
        }

    }

}