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


    /// PRIVATE


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

        when (val authorizationState = updateAuthorizationState.authorizationState) {
            is TdApi.AuthorizationStateWaitTdlibParameters -> {
                val text = "$tag: TDLib waits parameters"

                debugLog(text)

                systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
            }
            is TdApi.AuthorizationStateWaitEncryptionKey -> {
                val text = "$tag: TDLib waits encryption key (isEncrypted: ${authorizationState.isEncrypted})"

                debugLog(text)

                systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
            }
            is TdApi.AuthorizationStateWaitPhoneNumber -> {
                val text = "$tag: TDLib waits phone number"

                debugLog(text)

                systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
            }
            is TdApi.AuthorizationStateWaitCode -> onAuthorizationStateWaitCode(authorizationState, tag)
            is TdApi.AuthorizationStateReady -> {
                val text = "$tag: authorization complete"

                debugLog(text)

                systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
            }
            is TdApi.AuthorizationStateLoggingOut -> {
                val text = "$tag: logging out"

                debugLog(text)

                systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
            }
            is TdApi.AuthorizationStateClosed -> {
                val text = "$tag: AuthorizationStateClosed"

                debugLog(text)

                systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
            }
            is TdApi.AuthorizationStateWaitPassword -> {
                val text = "$tag: AuthorizationStateWaitPassword"

                debugLog(text)

                systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
            }
            else -> {
                val text = "$tag: $authorizationState"

                debugLog(text)

                systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))

                assertionFailure("Undefined authorizationState")
            }
        }
    }

    private fun onAuthorizationStateWaitCode(
        authorizationState: TdApi.AuthorizationStateWaitCode,
        tag: String
    ) {
        val codeInfo = authorizationState.codeInfo
        val isRegistered = authorizationState.isRegistered
        val termsOfService = authorizationState.termsOfService

        val extraSB = StringBuilder()
        if (termsOfService != null) {
            val minUserAge = termsOfService.minUserAge
            val showPopup = termsOfService.showPopup

            extraSB.append("minUserAge = $minUserAge; ")
            extraSB.append("showPopup = $showPopup; ")

            val text = termsOfService.text

            val termsOfServiceText = text.text
            extraSB.append("termsOfServiceText = $termsOfServiceText; ")
            if (text.entities.isNotEmpty()) {
                extraSB.append("text.entities = [\n")
            }

            text.entities.forEach { textEntity ->
                val length = textEntity.length
                val offset = textEntity.offset

                addTextEntityTypeInfoToExtraSB(textEntity.type, extraSB)

                extraSB.append("length = $length; ")
                extraSB.append("offset = $offset; ")
            }

            if (text.entities.isNotEmpty()) {
                extraSB.append("]\n")
            }
        }

        if (codeInfo != null) {
            val phoneNumber = codeInfo.phoneNumber
            val timeout = codeInfo.timeout

            codeInfo.type?.let {
                extraSB.append("codeInfo.type: ")
                addTypeInfoToExtraSB(it, extraSB)
            }

            codeInfo.nextType?.let {
                extraSB.append("codeInfo.nextType: ")
                addTypeInfoToExtraSB(it, extraSB)
            }

            extraSB.append("phoneNumber = $phoneNumber; ")
            extraSB.append("timeout = $timeout sec; ")
        }

        val text = "$tag: TDLib waits code (isRegistered = $isRegistered; $extraSB)"

        debugLog(text)

        systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
    }

    private fun onUpdateOption(
        tag: String,
        updateOption: TdApi.UpdateOption
    ) {
        tdUpdateOptionStack.push(updateOption)

        val updateOptionName = updateOption.name

        val updateOptionValue: Any =
            when (val unrecognizedUpdateOptionValue = updateOption.value) {
                is TdApi.OptionValueString -> unrecognizedUpdateOptionValue.value
                is TdApi.OptionValueInteger -> unrecognizedUpdateOptionValue.value
                is TdApi.OptionValueBoolean -> unrecognizedUpdateOptionValue.value
                is TdApi.OptionValueEmpty -> {
                    val text = "$tag: TdApi.OptionValueEmpty"

                    debugLog(text)

                    systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
                    ""
                }
                else -> {
                    val text = "$tag: Unspecified TdApi.OptionValue"

                    debugLog(text)

                    systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
                    assertionFailure("Undefined updateOptionValue")
                    ""
                }
            }

        val text = "$tag: $updateOptionName: $updateOptionValue"

        debugLog(text)

        systemMessageSendChannel.offer(createTGSystemMessageFromApp(text))
    }

    private fun addTextEntityTypeInfoToExtraSB(
        textEntityType: TdApi.TextEntityType,
        extraSB: StringBuilder
    ) {
        when (textEntityType) {
            is TdApi.TextEntityTypeMention -> {
                extraSB.append("TextEntityTypeMention = $textEntityType; ")
            }
            is TdApi.TextEntityTypeHashtag -> {
                extraSB.append("TextEntityTypeHashtag = $textEntityType; ")
            }
            is TdApi.TextEntityTypeCashtag -> {
                extraSB.append("TextEntityTypeCashtag = $textEntityType; ")
            }
            is TdApi.TextEntityTypeBotCommand -> {
                extraSB.append("TextEntityTypeBotCommand = $textEntityType; ")
            }
            is TdApi.TextEntityTypeUrl -> {
                extraSB.append("TextEntityTypeUrl = $textEntityType; ")
            }
            is TdApi.TextEntityTypeEmailAddress -> {
                extraSB.append("TextEntityTypeEmailAddress = $textEntityType; ")
            }
            is TdApi.TextEntityTypeBold -> {
                extraSB.append("TextEntityTypeBold = $textEntityType; ")
            }
            is TdApi.TextEntityTypeItalic -> {
                extraSB.append("TextEntityTypeItalic = $textEntityType; ")
            }
            is TdApi.TextEntityTypeCode -> {
                extraSB.append("TextEntityTypeCode = $textEntityType; ")
            }
            is TdApi.TextEntityTypePre -> {
                extraSB.append("TextEntityTypePre = $textEntityType; ")
            }
            is TdApi.TextEntityTypePreCode -> {
                extraSB.append(
                    "TextEntityTypePreCode = $textEntityType; " +
                            "language = ${textEntityType.language}"
                )
            }
            is TdApi.TextEntityTypeTextUrl -> {
                extraSB.append(
                    "TextEntityTypeTextUrl = $textEntityType; " +
                            "url = ${textEntityType.url}"
                )
            }
            is TdApi.TextEntityTypeMentionName -> {
                extraSB.append(
                    "TextEntityTypeMentionName = $textEntityType; " +
                            "userId = ${textEntityType.userId}"
                )
            }
            is TdApi.TextEntityTypePhoneNumber -> {
                extraSB.append("TextEntityTypePhoneNumber = $textEntityType; ")
            }
            else -> {
                debugLog("Unspecified textEntityType")
            }
        }
    }

    private fun addTypeInfoToExtraSB(
        authenticationCodeType: TdApi.AuthenticationCodeType,
        extraSB: StringBuilder
    ) {
        when (authenticationCodeType) {
            is TdApi.AuthenticationCodeTypeSms -> {
                extraSB.append("AuthenticationCodeTypeSms: length = ${authenticationCodeType.length}; ")
            }
            is TdApi.AuthenticationCodeTypeTelegramMessage -> {
                extraSB.append("AuthenticationCodeTypeTelegramMessage: length = ${authenticationCodeType.length}; ")
            }
            is TdApi.AuthenticationCodeTypeCall -> {
                extraSB.append("AuthenticationCodeTypeCall: length = ${authenticationCodeType.length}; ")
            }
            is TdApi.AuthenticationCodeTypeFlashCall -> {
                extraSB.append("AuthenticationCodeTypeFlashCall: pattern = ${authenticationCodeType.pattern}; ")
            }
            else -> {
                debugLog("Unspecified authenticationCodeType")
            }
        }
    }

}
