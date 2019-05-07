package com.anvipo.angram.businessLogicLayer.di

import com.anvipo.angram.applicationLayer.launchSystem.App
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGateway
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.TDLibGatewayImp
import org.drinkless.td.libcore.telegram.Client
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

object GatewaysModule {


    private val updatesExceptionHandler: StringQualifier = named("updatesExceptionHandler")
    private val updatesExceptionHandlerFunction: StringQualifier = named("updatesExceptionHandlerFunction")

    private val defaultExceptionHandler: StringQualifier = named("defaultExceptionHandler")
    private val defaultExceptionHandlerFunction: StringQualifier = named("defaultExceptionHandlerFunction")

    val module: Module = module {

        // ----------------------------- TG -------------------------

        single {
            val app = androidApplication() as App
            app.updatesHandlerFunction
        }
        single {
            Client.ResultHandler(get())
        }

        single(updatesExceptionHandlerFunction) {
            val app = androidApplication() as App
            app.updatesExceptionHandlerFunction
        }
        single(updatesExceptionHandler) {
            Client.ExceptionHandler(get(updatesExceptionHandlerFunction))
        }

        single(defaultExceptionHandlerFunction) {
            val app = androidApplication() as App
            app.defaultExceptionHandlerFunction
        }
        single(defaultExceptionHandler) {
            Client.ExceptionHandler(get(defaultExceptionHandlerFunction))
        }

        single<Client> {
            Client.create(
                get(),
                get(updatesExceptionHandler),
                get(defaultExceptionHandler)
            )
        }

        // ----------------------------- TG -------------------------


        // ---------------------------- GATEWAYS ---------------------

        single<TDLibGateway> {
            TDLibGatewayImp(tgClient = get())
        }

    }

}