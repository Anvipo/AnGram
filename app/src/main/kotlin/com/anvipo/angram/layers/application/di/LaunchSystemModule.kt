package com.anvipo.angram.layers.application.di

import com.anvipo.angram.layers.core.collections.stack.IMutableStack
import com.anvipo.angram.layers.core.collections.stack.IReadOnlyStack
import com.anvipo.angram.layers.core.collections.stack.IStack
import com.anvipo.angram.layers.core.collections.stack.MyStack
import com.anvipo.angram.layers.global.types.*
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

@Suppress("EXPERIMENTAL_API_USAGE")
object LaunchSystemModule {

    val tdApiUpdateAuthorizationStateIMutableStackQualifier: StringQualifier =
        named("tdApiUpdateAuthorizationStateIMutableStack")
    private val tdApiUpdateAuthorizationStateIReadOnlyStackQualifier: StringQualifier =
        named("tdApiUpdateAuthorizationStateIReadOnlyStack")
    private val tdApiUpdateAuthorizationStateIStackQualifier = named("tdApiUpdateAuthorizationStateIStack")

    val tdApiUpdateOptionIMutableStackQualifier: StringQualifier = named("tdApiUpdateOptionIMutableStack")
    private val tdApiUpdateOptionIReadOnlyStackQualifier = named("tdApiUpdateOptionIReadOnlyStack")
    private val tdApiUpdateOptionIStackQualifier = named("tdApiUpdateOptionIStack")

    val tdApiUpdateConnectionStateIMutableStackQualifier: StringQualifier =
        named("tdUpdateApiConnectionStateIMutableStack")
    private val tdApiUpdateConnectionStateIReadOnlyStackQualifier =
        named("tdApiUpdateConnectionStateIReadOnlyStack")
    private val tdApiUpdateConnectionStateIStackQualifier = named("tdApiUpdateConnectionStateIStack")

    val tdLibDefaultExceptionsIMutableStackQualifier: StringQualifier =
        named("tdLibDefaultExceptionsIMutableStack")
    private val tdLibDefaultExceptionsIReadOnlyStackQualifier =
        named("tdLibDefaultExceptionsIReadOnlyStack")
    private val tdLibDefaultExceptionsIStackQualifier = named("tdLibDefaultExceptionsIStack")

    val tdLibUpdatesExceptionsIMutableStackQualifier: StringQualifier =
        named("tdLibUpdatesExceptionsIMutableStack")
    private val tdLibUpdatesExceptionsIReadOnlyStackQualifier =
        named("tdLibUpdatesExceptionsIReadOnlyStack")
    private val tdLibUpdatesExceptionsIStackQualifier = named("tdLibUpdatesExceptionsIStack")

    val tdApiUpdatesIMutableStackQualifier: StringQualifier = named("tdApiUpdatesIMutableStack")
    private val tdApiUpdatesIReadOnlyStackQualifier = named("tdApiUpdatesIReadOnlyStack")
    private val tdApiUpdatesIStackQualifier = named("tdApiUpdatesIStack")

    val tdApiObjectIMutableStackQualifier: StringQualifier = named("tdLibUpdatesIMutableStack")
    private val tdApiObjectIReadOnlyStackQualifier = named("tdLibUpdatesIReadOnlyStack")
    private val tdApiObjectIStackQualifier = named("tdLibUpdatesIStack")

    @Suppress("RemoveExplicitTypeArguments")
    val module: Module = module {

        single<IReadOnlyStack<TdApiUpdateAuthorizationState>>(
            tdApiUpdateAuthorizationStateIReadOnlyStackQualifier
        ) {
            get(tdApiUpdateAuthorizationStateIStackQualifier)
        }
        single<IMutableStack<TdApiUpdateAuthorizationState>>(
            tdApiUpdateAuthorizationStateIMutableStackQualifier
        ) {
            get(tdApiUpdateAuthorizationStateIStackQualifier)
        }
        single<IStack<TdApiUpdateAuthorizationState>>(tdApiUpdateAuthorizationStateIStackQualifier) {
            MyStack<TdApiUpdateAuthorizationState>()
        }

        single<IReadOnlyStack<TdApiUpdateOption>>(tdApiUpdateOptionIReadOnlyStackQualifier) {
            get(tdApiUpdateOptionIStackQualifier)
        }
        single<IMutableStack<TdApiUpdateOption>>(tdApiUpdateOptionIMutableStackQualifier) {
            get(tdApiUpdateOptionIStackQualifier)
        }
        single<IStack<TdApiUpdateOption>>(tdApiUpdateOptionIStackQualifier) {
            MyStack<TdApiUpdateOption>()
        }

        single<IReadOnlyStack<TdApiUpdateConnectionState>>(
            tdApiUpdateConnectionStateIReadOnlyStackQualifier
        ) {
            get(tdApiUpdateConnectionStateIStackQualifier)
        }
        single<IMutableStack<TdApiUpdateConnectionState>>(tdApiUpdateConnectionStateIMutableStackQualifier) {
            get(tdApiUpdateConnectionStateIStackQualifier)
        }
        single<IStack<TdApiUpdateConnectionState>>(tdApiUpdateConnectionStateIStackQualifier) {
            MyStack<TdApiUpdateConnectionState>()
        }

        single<IReadOnlyStack<TDLibDefaultException>>(tdLibDefaultExceptionsIReadOnlyStackQualifier) {
            get(tdLibDefaultExceptionsIStackQualifier)
        }
        single<IMutableStack<TDLibDefaultException>>(tdLibDefaultExceptionsIMutableStackQualifier) {
            get(tdLibDefaultExceptionsIStackQualifier)
        }
        single<IStack<TDLibDefaultException>>(tdLibDefaultExceptionsIStackQualifier) {
            MyStack<TDLibUpdatesException>()
        }


        single<IReadOnlyStack<TDLibUpdatesException>>(tdLibUpdatesExceptionsIReadOnlyStackQualifier) {
            get(tdLibUpdatesExceptionsIStackQualifier)
        }
        single<IMutableStack<TDLibUpdatesException>>(tdLibUpdatesExceptionsIMutableStackQualifier) {
            get(tdLibUpdatesExceptionsIStackQualifier)
        }
        single<IStack<TDLibUpdatesException>>(tdLibUpdatesExceptionsIStackQualifier) {
            MyStack<TDLibUpdatesException>()
        }


        single<IReadOnlyStack<TdApiUpdate>>(tdApiUpdatesIReadOnlyStackQualifier) {
            get(tdApiUpdatesIStackQualifier)
        }
        single<IMutableStack<TdApiUpdate>>(tdApiUpdatesIMutableStackQualifier) {
            get(tdApiUpdatesIStackQualifier)
        }
        single<IStack<TdApiUpdate>>(tdApiUpdatesIStackQualifier) {
            MyStack<TdApiUpdate>()
        }


        single<IReadOnlyStack<TdApiObject>>(tdApiObjectIReadOnlyStackQualifier) {
            get(tdApiObjectIStackQualifier)
        }
        single<IMutableStack<TdApiObject>>(tdApiObjectIMutableStackQualifier) {
            get(tdApiObjectIStackQualifier)
        }
        single<IStack<TdApiObject>>(tdApiObjectIStackQualifier) {
            MyStack<TdApiObject>()
        }

    }

}
