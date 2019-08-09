package com.anvipo.angram.applicationLayer.launchSystem

import android.app.Application
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.applicationLayer.coordinator.di.ApplicationCoordinatorModule
import com.anvipo.angram.applicationLayer.di.LaunchSystemModule
import com.anvipo.angram.applicationLayer.di.LaunchSystemModule.connectionStateAppSendChannelQualifier
import com.anvipo.angram.applicationLayer.di.LaunchSystemModule.systemMessageSendChannelQualifier
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule
import com.anvipo.angram.applicationLayer.types.ConnectionStateSendChannel
import com.anvipo.angram.applicationLayer.types.SystemMessageSendChannel
import com.anvipo.angram.businessLogicLayer.di.UseCasesModule
import com.anvipo.angram.coreLayer.CoreErrors
import com.anvipo.angram.coreLayer.CoreHelpers.assertionFailure
import com.anvipo.angram.coreLayer.CoreHelpers.debugLog
import com.anvipo.angram.coreLayer.collections.IMutableStack
import com.anvipo.angram.coreLayer.collections.MutableStack
import com.anvipo.angram.coreLayer.message.SystemMessage
import com.anvipo.angram.coreLayer.message.SystemMessageType
import com.anvipo.angram.dataLayer.di.GatewaysModule
import com.anvipo.angram.global.GlobalHelpers.createTGSystemMessageFromApp
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.addProxy.di.AddProxyModule
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterAuthenticationPassword.di.EnterAuthenticationPasswordModule
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterPhoneNumber.di.EnterPhoneNumberModule
import com.anvipo.angram.presentationLayer.flows.authFlow.screens.enterPhoneNumber.di.EnterPhoneNumberModule.connectionStateEnterPhoneNumberSendChannelQualifier
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.error.*

class App : Application() {

    companion object {
        const val TAG: String = "AnGram"
    }

    override fun onCreate() {
        super.onCreate()
        initDI()
    }


    private fun initDI() {
        val debugModules = listOf(
            LaunchSystemModule.module,
            SystemInfrastructureModule.module,
            UseCasesModule.module,
            GatewaysModule.module,
            ApplicationCoordinatorModule.module,
            EnterPhoneNumberModule.module,
            EnterAuthenticationCodeModule.module,
            EnterAuthenticationPasswordModule.module,
            AddProxyModule.module
        )

        if (BuildConfig.DEBUG) {
            startKoin {
                androidLogger()

                androidContext(this@App)

                modules(debugModules)
            }
        } else {
            // TODO: release config
            debugLog("TODO")
        }
    }

    private val systemMessageSendChannel: SystemMessageSendChannel
            by inject(systemMessageSendChannelQualifier)

    private val connectionStateAppSendChannel: ConnectionStateSendChannel
            by inject(connectionStateAppSendChannelQualifier)

    private val connectionStateEnterPhoneNumberSendChannel: ConnectionStateSendChannel
            by inject(connectionStateEnterPhoneNumberSendChannelQualifier)


    // ------- TG Client properties and methods


    private val tdObjectsStack: IMutableStack<TdApi.Object> = MutableStack()

    private val tdUpdateStack: IMutableStack<TdApi.Update> = MutableStack()

    private val tdUpdateOptionStack: IMutableStack<TdApi.UpdateOption> = MutableStack()

    private val tdUpdateUserStack: IMutableStack<TdApi.UpdateUser> = MutableStack()
    private val tdUpdateHavePendingNotificationsStack: IMutableStack<TdApi.UpdateHavePendingNotifications> =
        MutableStack()

    private val tdUpdateScopeNotificationSettingsStack: IMutableStack<TdApi.UpdateScopeNotificationSettings> =
        MutableStack()
    private val tdUpdateTermsOfServiceStack: IMutableStack<TdApi.UpdateTermsOfService> = MutableStack()

    private val tdUpdateAuthorizationStateStack: IMutableStack<TdApi.UpdateAuthorizationState> = MutableStack()

    private val tdUpdateConnectionStateStack: IMutableStack<TdApi.UpdateConnectionState> = MutableStack()

    private val tdErrorsStack: IMutableStack<Throwable> = MutableStack()
    private val tdObjectsAndErrorsStack: IMutableStack<Any> = MutableStack()


    internal val updatesHandlerFunction: (TdApi.Object) -> Unit = { tdApiObject ->
        val tag = "${this::class.java.simpleName} updatesHandler"

        tdObjectsStack.push(tdApiObject)
        tdObjectsAndErrorsStack.push(tdApiObject)

        when (tdApiObject) {
            is TdApi.Update -> onUpdate(tag, tdApiObject)
            else -> {
                // TODO: handle this case
                val text = tdApiObject.toString()

                debugLog(text)
                systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))

                assertionFailure("Undefined tdApiObject")
            }
        }
    }

    internal val updatesExceptionHandlerFunction: (Throwable) -> Unit = { error ->
        tdErrorsStack.push(error)
        tdObjectsAndErrorsStack.push(error)

        val text = error.localizedMessage

        debugLog(text)
        when (error) {
            is NoBeanDefFoundException,
            is BadScopeInstanceException,
            is DefinitionOverrideException,
            is KoinAppAlreadyStartedException,
            is MissingPropertyException,
            is NoParameterFoundException,
            is NoPropertyFileFoundException,
            is NoScopeDefinitionFoundException,
            is ScopeAlreadyCreatedException,
            is ScopeNotCreatedException,
            is InstanceCreationException -> {
                val koinExceptionMessage = SystemMessage(
                    text = text,
                    type = SystemMessageType.ALERT,
                    shouldBeShownToUser = BuildConfig.DEBUG,
                    shouldBeShownInLogs = false
                )

                systemMessageSendChannel.offer(koinExceptionMessage)
            }
            is CoreErrors.DebugError -> systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
            else -> systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
        }
    }

    internal val defaultExceptionHandlerFunction: (Throwable) -> Unit = { error ->
        tdErrorsStack.push(error)
        tdObjectsAndErrorsStack.push(error)

        val text = error.localizedMessage

        debugLog(text)
        systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
    }


    private fun onUpdate(
        tag: String,
        tdApiUpdate: TdApi.Update
    ) {
        tdUpdateStack.push(tdApiUpdate)

        when (tdApiUpdate) {
            is TdApi.UpdateConnectionState -> onUpdateConnectionState(tag, tdApiUpdate)
            is TdApi.UpdateAuthorizationState -> onUpdateAuthorizationState(tag, tdApiUpdate)
            is TdApi.UpdateOption -> onUpdateOption(tag, tdApiUpdate)
            is TdApi.UpdateUser -> {
                tdUpdateUserStack.push(tdApiUpdate)
                debugLog(tdApiUpdate.toString())
            }
            is TdApi.UpdateHavePendingNotifications -> {
                tdUpdateHavePendingNotificationsStack.push(tdApiUpdate)
                debugLog(tdApiUpdate.toString())
            }
            is TdApi.UpdateScopeNotificationSettings -> {
                tdUpdateScopeNotificationSettingsStack.push(tdApiUpdate)
                debugLog(tdApiUpdate.toString())
            }
            is TdApi.UpdateTermsOfService -> {
                tdUpdateTermsOfServiceStack.push(tdApiUpdate)
                debugLog(tdApiUpdate.toString())
            }
            else -> {
                val text = tdApiUpdate.toString()

                debugLog(text)

                systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))

                assertionFailure("Unspecified tdApiUpdate")
            }
        }
    }

    private fun onUpdateConnectionState(
        tag: String,
        tdApiUpdateConnectionState: TdApi.UpdateConnectionState
    ) {
        tdUpdateConnectionStateStack.push(tdApiUpdateConnectionState)
        connectionStateAppSendChannel.offer(tdApiUpdateConnectionState.state)
        connectionStateEnterPhoneNumberSendChannel.offer(tdApiUpdateConnectionState.state)

        val text = "$tag: connection state updated (${tdApiUpdateConnectionState.state})"

        debugLog(text)
        systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
    }

    private fun onUpdateAuthorizationState(
        tag: String,
        updateAuthorizationState: TdApi.UpdateAuthorizationState
    ) {
        tdUpdateAuthorizationStateStack.push(updateAuthorizationState)

        val text = "$tag: ${updateAuthorizationState.authorizationState}"

        debugLog(text)
        systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
    }

    private fun onUpdateOption(
        tag: String,
        updateOption: TdApi.UpdateOption
    ) {
        tdUpdateOptionStack.push(updateOption)

        val text = "$tag: ${updateOption.name}: ${updateOption.value}"

        debugLog(text)
        systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
    }

}
