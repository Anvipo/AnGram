package com.anvipo.angram.layers.application.launchSystem

import android.app.Application
import com.anvipo.angram.layers.application.coordinator.di.ApplicationCoordinatorModule
import com.anvipo.angram.layers.application.di.LaunchSystemModule
import com.anvipo.angram.layers.application.di.LaunchSystemModule.connectionStateAppSendChannelQualifier
import com.anvipo.angram.layers.application.di.LaunchSystemModule.enabledProxyIdSendChannelQualifier
import com.anvipo.angram.layers.application.di.LaunchSystemModule.systemMessageSendChannelQualifier
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule
import com.anvipo.angram.layers.application.types.ConnectionStateSendChannel
import com.anvipo.angram.layers.application.types.EnabledProxyIdSendChannel
import com.anvipo.angram.layers.application.types.SystemMessageSendChannel
import com.anvipo.angram.layers.businessLogic.di.UseCasesModule
import com.anvipo.angram.layers.core.CoreHelpers.IS_IN_DEBUG_MODE
import com.anvipo.angram.layers.core.CoreHelpers.assertionFailure
import com.anvipo.angram.layers.core.HasLogger
import com.anvipo.angram.layers.core.collections.IMutableStack
import com.anvipo.angram.layers.core.collections.MutableStack
import com.anvipo.angram.layers.core.message.SystemMessage
import com.anvipo.angram.layers.core.message.SystemMessageType
import com.anvipo.angram.layers.data.di.GatewaysModule
import com.anvipo.angram.layers.global.GlobalHelpers.createTGSystemMessage
import com.anvipo.angram.layers.presentation.common.interfaces.HasMyCoroutineBuilders
import com.anvipo.angram.layers.presentation.flows.auth.coordinator.di.AuthorizationCoordinatorModule
import com.anvipo.angram.layers.presentation.flows.auth.screens.addProxy.di.AddProxyModule
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationCode.di.EnterAuthenticationCodeModule
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterAuthenticationPassword.di.EnterAuthenticationPasswordModule
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterPhoneNumber.di.EnterPhoneNumberModule
import com.anvipo.angram.layers.presentation.flows.auth.screens.enterPhoneNumber.di.EnterPhoneNumberModule.connectionStateEnterPhoneNumberSendChannelQualifier
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.error.*
import kotlin.coroutines.CoroutineContext

class App :
    Application(),
    HasLogger,
    HasMyCoroutineBuilders {

    companion object {
        private lateinit var INSTANCE: App
    }

    override val jobsThatMustBeCancelledInLifecycleEnd: MutableList<Job> = mutableListOf()

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override val className: String = this::class.java.name

    override fun onCreate() {
        super.onCreate()
        initDI()
        INSTANCE = this
    }

    override fun onTerminate() {
        super.onTerminate()

        val methodName = object {}.javaClass.enclosingMethod!!.name
        val cancellationException = CancellationException("$className::$methodName")

        jobsThatMustBeCancelledInLifecycleEnd.forEach { it.cancel(cancellationException) }
    }


    override fun <T : Any> additionalLogging(logObj: T) {
        myLaunch {
            val systemMessage: SystemMessage = when (logObj) {
                is String -> createTGSystemMessage(logObj)
                is SystemMessage -> logObj
                is Unit -> return@myLaunch
                else -> {
                    val message = "Undefined logObj type = $logObj"
                    assertionFailure(message)
                    TODO(message)
                }
            }

            systemMessageSendChannel.send(systemMessage)
        }
    }

    override fun myLaunchExceptionHandler(throwable: Throwable) {
        additionalLogging(throwable.localizedMessage)
    }


    fun handleUpdates(tdApiObject: TdApi.Object) {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name
        myLog(
            invokationPlace = invokationPlace,
            currentParameters = "tdApiObject = $tdApiObject"
        )

        tdObjectsStack.push(tdApiObject)

        when (tdApiObject) {
            is TdApi.Update -> onUpdate(tdApiObject)
            else -> myLog(
                currentParameters = "Undefined td api object = $tdApiObject",
                invokationPlace = invokationPlace
            )
        }
    }

    fun handleUpdatesException(error: Throwable) {
        val text = "error.localizedMessage = ${error.localizedMessage}"

        val systemMessage: SystemMessage =
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
                    SystemMessage(
                        text = text,
                        type = SystemMessageType.ALERT,
                        shouldBeShownToUser = IS_IN_DEBUG_MODE,
                        shouldBeShownInLogs = false
                    )
                }
                else -> createTGSystemMessage(text)
            }

        val customLogging: () -> Unit = {
            additionalLogging(systemMessage)
        }

        myLog(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            currentParameters = text,
            customLogging = customLogging
        )

        tdErrorsStack.push(error)
    }

    fun handleDefaultException(throwable: Throwable) {
        myLog(
            invokationPlace = object {}.javaClass.enclosingMethod!!.name,
            currentParameters = "error.localizedMessage = ${throwable.localizedMessage}"
        )

        tdErrorsStack.push(throwable)
    }


    private val systemMessageSendChannel: SystemMessageSendChannel
            by inject(systemMessageSendChannelQualifier)

    private val enabledProxyIdSendChannel: EnabledProxyIdSendChannel
            by inject(enabledProxyIdSendChannelQualifier)

    private val connectionStateAppSendChannel: ConnectionStateSendChannel
            by inject(connectionStateAppSendChannelQualifier)

    private val connectionStateEnterPhoneNumberSendChannel: ConnectionStateSendChannel
            by inject(connectionStateEnterPhoneNumberSendChannelQualifier)


    // ------- TG Client properties and methods


    private val tdObjectsStack: IMutableStack<TdApi.Object> = MutableStack()

    private val tdUpdateStack: IMutableStack<TdApi.Update> = MutableStack()

    private val tdErrorsStack: IMutableStack<Throwable> = MutableStack()

    private fun initDI() {
        val invokationPlace = object {}.javaClass.enclosingMethod!!.name

        val debugModules = listOf(
            LaunchSystemModule.module,
            SystemInfrastructureModule.module,
            UseCasesModule.module,
            GatewaysModule.module,
            ApplicationCoordinatorModule.module,
            AuthorizationCoordinatorModule.module,
            EnterPhoneNumberModule.module,
            EnterAuthenticationCodeModule.module,
            EnterAuthenticationPasswordModule.module,
            AddProxyModule.module
        )

        if (IS_IN_DEBUG_MODE) {
            startKoin {
                androidLogger()

                androidContext(this@App)

                modules(debugModules)
            }
        } else {
            // TODO: release config
            myLog(
                invokationPlace = invokationPlace,
                currentParameters = "TODO"
            )
        }

        myLog(invokationPlace = invokationPlace)
    }


    private fun onUpdate(
        tdApiUpdate: TdApi.Update
    ) {
        tdUpdateStack.push(tdApiUpdate)

        when (tdApiUpdate) {
            is TdApi.UpdateConnectionState -> onUpdateConnectionState(tdApiUpdate)
            is TdApi.UpdateOption -> onUpdateOption(tdApiUpdate)
        }
    }

    private fun onUpdateConnectionState(
        tdApiUpdateConnectionState: TdApi.UpdateConnectionState
    ) {
        val connectionState = tdApiUpdateConnectionState.state
        connectionStateAppSendChannel.offer(connectionState)
        connectionStateEnterPhoneNumberSendChannel.offer(connectionState)
    }

    private fun onUpdateOption(
        updateOption: TdApi.UpdateOption
    ) {
        val optionValue = updateOption.value

        if (updateOption.name == "enabled_proxy_id") {
            when (optionValue) {
                is TdApi.OptionValueInteger -> enabledProxyIdSendChannel.offer(optionValue.value)
                is TdApi.OptionValueEmpty -> enabledProxyIdSendChannel.offer(null)
                else -> assertionFailure("Undefined enabled_proxy_id value = ${optionValue}")
            }
        }
    }

}
