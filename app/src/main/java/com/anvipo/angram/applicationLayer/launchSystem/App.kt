package com.anvipo.angram.applicationLayer.launchSystem

import android.annotation.SuppressLint
import android.app.Application
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.applicationLayer.di.LaunchSystemModule
import com.anvipo.angram.applicationLayer.di.SystemInfrastructureModule
import com.anvipo.angram.applicationLayer.navigation.coordinator.di.ApplicationRootCoordinatorModule
import com.anvipo.angram.businessLogicLayer.di.GatewaysModule
import com.anvipo.angram.businessLogicLayer.di.UseCasesModule
import com.anvipo.angram.coreLayer.assertionFailure
import com.anvipo.angram.coreLayer.collections.IMutableStack
import com.anvipo.angram.coreLayer.collections.MutableStack
import com.anvipo.angram.coreLayer.debugLog
import com.anvipo.angram.coreLayer.message.ISentDataNotifier
import com.anvipo.angram.coreLayer.message.SystemMessage
import com.anvipo.angram.coreLayer.message.SystemMessageType
import com.anvipo.angram.global.createTGSystemMessageFromApp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.AuthUserStoryModule
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.error.*
import java.util.*

class App : Application() {

    companion object {
        lateinit var appCode: String
            private set

        const val TAG: String = "AnGram"
    }

    override fun onCreate() {
        super.onCreate()
        appCode = UUID.randomUUID().toString()

        initDI()
    }


    /// PRIVATE


    private fun initDI() {
        if (BuildConfig.DEBUG) {
            // start Koin!

            startKoin {
                // Android context
                androidContext(this@App)

                androidLogger()

                // modules
                modules(
                    LaunchSystemModule.module,
                    SystemInfrastructureModule.module,
                    UseCasesModule.module,
                    GatewaysModule.module,
                    ApplicationRootCoordinatorModule.module,
                    AuthUserStoryModule.module
                )
            }
        } else {
            // TODO
            debugLog("TODO")
        }
    }

    private val systemMessageNotifier: ISentDataNotifier<SystemMessage> by inject()


    // ------- TG Client properties and methods


    private val tdObjectsStack: IMutableStack<TdApi.Object> = MutableStack()

    private val tdUpdateStack: IMutableStack<TdApi.Update> = MutableStack()

    private val tdUpdateOptionStack: IMutableStack<TdApi.UpdateOption> = MutableStack()

    @Suppress("RemoveExplicitTypeArguments")
    private val tdUpdateAuthorizationStateStack: IMutableStack<TdApi.UpdateAuthorizationState>
            by inject()

    private val tdUpdateConnectionStateStack: IMutableStack<TdApi.UpdateConnectionState> = MutableStack()

    private val tdErrorsStack: IMutableStack<Throwable> = MutableStack()
    private val tdObjectsAndErrorsStack: IMutableStack<Any> = MutableStack()


    internal val updatesHandlerFunction: (TdApi.Object) -> Unit = { tdApiObject ->
        val tag = "${this::class.java.simpleName} updatesHandler"

        tdObjectsStack.push(tdApiObject)
        tdObjectsAndErrorsStack.push(tdApiObject)

        @SuppressLint("SyntheticAccessor")
        when (tdApiObject) {
            is TdApi.Update -> onUpdate(tag, tdApiObject)
            else -> {
                // TODO: handle this case
                val text = tdApiObject.toString()

                debugLog(text)

                systemMessageNotifier.send(createTGSystemMessageFromApp(text))

                assertionFailure()
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

                systemMessageNotifier.send(koinExceptionMessage)
            }
            else -> systemMessageNotifier.send(createTGSystemMessageFromApp(text))
        }
    }

    internal val defaultExceptionHandlerFunction: (Throwable) -> Unit = { error ->
        tdErrorsStack.push(error)
        tdObjectsAndErrorsStack.push(error)

        val text = error.localizedMessage

        debugLog(text)

        systemMessageNotifier.send(createTGSystemMessageFromApp(text))
    }


    @SuppressLint("SyntheticAccessor")
    private fun onUpdate(
        tag: String,
        tdApiUpdate: TdApi.Update
    ) {
        tdUpdateStack.push(tdApiUpdate)

        when (tdApiUpdate) {
            is TdApi.UpdateOption -> onUpdateOption(tag, tdApiUpdate)
            is TdApi.UpdateAuthorizationState -> onUpdateAuthorizationState(tag, tdApiUpdate)
            is TdApi.UpdateConnectionState -> onUpdateConnectionState(tag, tdApiUpdate)
            else -> {
                // TODO: handle this case

                val text = tdApiUpdate.toString()

                debugLog(text)

                systemMessageNotifier.send(createTGSystemMessageFromApp(text))

                assertionFailure()
            }
        }
    }

    @SuppressLint("SyntheticAccessor")
    private fun onUpdateConnectionState(
        tag: String,
        tdApiUpdateConnectionState: TdApi.UpdateConnectionState
    ) {
        tdUpdateConnectionStateStack.push(tdApiUpdateConnectionState)

        val state: String = when (val connectionState = tdApiUpdateConnectionState.state) {
            is TdApi.ConnectionStateConnecting -> "connecting"
            is TdApi.ConnectionStateReady -> "ready"
            else -> {
                // TODO: handle this case

                val text = connectionState.toString()

                debugLog(text)

                systemMessageNotifier.send(createTGSystemMessageFromApp(text))

                assertionFailure()

                ""
            }
        }

        val text = "$tag: connection state updated ($state)"

        debugLog(text)

        systemMessageNotifier.send(createTGSystemMessageFromApp(text))

        // TODO: show connection state
    }

    @SuppressLint("SyntheticAccessor")
    private fun onUpdateAuthorizationState(
        tag: String,
        updateAuthorizationState: TdApi.UpdateAuthorizationState
    ) {
        tdUpdateAuthorizationStateStack.push(updateAuthorizationState)

        when (val authorizationState = updateAuthorizationState.authorizationState) {
            is TdApi.AuthorizationStateWaitTdlibParameters -> {
                val text = "$tag: TDLib waits parameters"

                debugLog(text)

                systemMessageNotifier.send(createTGSystemMessageFromApp(text))
            }
            is TdApi.AuthorizationStateWaitEncryptionKey -> {
                val text = "$tag: TDLib waits encryption key (isEncrypted: ${authorizationState.isEncrypted})"

                debugLog(text)

                systemMessageNotifier.send(createTGSystemMessageFromApp(text))
            }
            is TdApi.AuthorizationStateWaitPhoneNumber -> {
                val text = "$tag: TDLib waits phone number"

                debugLog(text)

                systemMessageNotifier.send(createTGSystemMessageFromApp(text))
            }
            is TdApi.AuthorizationStateWaitCode -> onAuthorizationStateWaitCode(authorizationState, tag)
            else -> {
                // TODO: handle this case

                val text = "$tag: $authorizationState"

                debugLog(text)

                systemMessageNotifier.send(createTGSystemMessageFromApp(text))

                assertionFailure()
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

        systemMessageNotifier.send(createTGSystemMessageFromApp(text))
    }

    @SuppressLint("SyntheticAccessor")
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

                    systemMessageNotifier.send(createTGSystemMessageFromApp(text))
                    assertionFailure()
                    ""
                }
                else -> {
                    val text = "$tag: Unspecified TdApi.OptionValue"

                    debugLog(text)

                    systemMessageNotifier.send(createTGSystemMessageFromApp(text))
                    assertionFailure()
                    ""
                }
            }

        val text = "$tag: $updateOptionName: $updateOptionValue"

        debugLog(text)

        systemMessageNotifier.send(createTGSystemMessageFromApp(text))
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
