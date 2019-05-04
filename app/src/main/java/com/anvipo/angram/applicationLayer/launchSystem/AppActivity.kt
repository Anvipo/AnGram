package com.anvipo.angram.applicationLayer.launchSystem

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import com.anvipo.angram.R
import com.anvipo.angram.applicationLayer.assemblies.LaunchSystemAssembly
import com.anvipo.angram.global.ActivityFinishError
import com.anvipo.angram.global.assertionFailure
import com.anvipo.angram.global.debugLog
import com.anvipo.angram.global.debugLogAndToast
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseFragment
import com.anvipo.angram.presentationLayer.common.interfaces.Coordinatorable
import com.anvipo.angram.presentationLayer.common.interfaces.NavigationController
import com.anvipo.angram.presentationLayer.common.interfaces.Presentable
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import java.lang.ref.WeakReference
import java.util.*

class AppActivity :
    AppCompatActivity(),
    NavigationController {

    override val thisContext: Context
        get() = this

    override var onBackPressed: (() -> Unit)? = {
        this.onBackPressed()
    }

    override val topViewController: Presentable?
        get() {
            if (supportFragmentManager.fragments.isEmpty()) {
                return null
            }

            val topViewController = supportFragmentManager.fragments.last()

            return topViewController as Presentable
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        changeThemeFromSplashToApp()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_container)

        weakInstance = WeakReference(this)

        tgClient = LaunchSystemAssembly.tgClient

        applicationCoordinator.start()
    }


    override fun onBackPressed() {
        currentFragment?.onBackPressed?.invoke() ?: super.onBackPressed()
    }


    /// NAVIGATION CONTROLLER


    override fun push(
        viewController: Presentable,
        hideTabBar: Boolean,
        completion: (() -> Unit)?
    ) {
        supportFragmentManager.transaction {
            replace(R.id.container, viewController as Fragment)
            addToBackStack(null)
        }

        completion?.invoke()
    }

    override fun setRootViewController(
        viewController: Presentable,
        completion: (() -> Unit)?
    ) {
        repeat(supportFragmentManager.fragments.size) {
            supportFragmentManager.popBackStackImmediate()
        }

        try {
            supportFragmentManager.transaction(now = true) {
                replace(R.id.container, viewController as Fragment)
                runOnCommit { completion?.invoke() }
            }
        } catch (ise: IllegalStateException) {
            debugLog("Turn on your screen, mate")

            // useful to read: https://www.androiddesignpatterns.com/2013/08/fragment-transaction-commit-state-loss.html

            supportFragmentManager.transaction(now = true, allowStateLoss = true) {
                replace(R.id.container, viewController as Fragment)
                runOnCommit { completion?.invoke() }
            }
        } catch (e: Exception) {
            debugLog(e.localizedMessage)

            supportFragmentManager.transaction(now = true, allowStateLoss = true) {
                replace(R.id.container, viewController as Fragment)
                runOnCommit { completion?.invoke() }
            }
        }
    }

    @Throws(ActivityFinishError::class)
    override fun popViewController(): Presentable? {
        if (supportFragmentManager.fragments.isEmpty()) {
            return null
        }

        if (supportFragmentManager.backStackEntryCount < 1) {
            super.onBackPressed()
            throw ActivityFinishError(message = "supportFragmentManager.backStackEntryCount < 1.")
        }

        val poppedViewController = supportFragmentManager.fragments.last()

        supportFragmentManager.popBackStackImmediate()

        if (poppedViewController == null) {
            return null
        }

        return poppedViewController as Presentable
    }

    override var isNavigationBarHidden: Boolean = false


    /// PRIVATE


    private fun changeThemeFromSplashToApp() {
        setTheme(R.style.AppTheme)
    }


    private val currentFragment: BaseFragment?
        get() = supportFragmentManager.findFragmentById(R.id.container) as BaseFragment


    /// DEPENDENCIES


    private val applicationCoordinator: Coordinatorable by lazy {
        LaunchSystemAssembly.applicationCoordinator
    }

    ///


    @Suppress("MemberVisibilityCanBePrivate")
    companion object StaticAppActivity {
        lateinit var tgClient: Client
            private set

        lateinit var weakInstance: WeakReference<NavigationController>
            private set

        internal val instance: AppActivity?
            @SuppressLint("SyntheticAccessor")
            get() = weakInstance.get() as? AppActivity


        private val tdObjectsStack: Stack<TdApi.Object> = Stack()

        private val tdUpdateStack: Stack<TdApi.Update> = Stack()

        private val tdUpdateOptionStack: Stack<TdApi.UpdateOption> = Stack()

        private val tdUpdateAuthorizationStateStack: Stack<TdApi.UpdateAuthorizationState> = Stack()
        internal val lastAuthorizationState: TdApi.UpdateAuthorizationState
            @SuppressLint("SyntheticAccessor")
            get() = tdUpdateAuthorizationStateStack.peek()

        private val tdUpdateConnectionStateStack: Stack<TdApi.UpdateConnectionState> = Stack()

        private val tdErrorsStack: Stack<Throwable> = Stack()
        private val tdObjectsAndErrorsStack: Stack<Any> = Stack()


        internal val updatesHandler = Client.ResultHandler { tdApiObject ->
            val tag = "${this::class.java.simpleName} updatesHandler"

            tdObjectsStack.push(tdApiObject)
            tdObjectsAndErrorsStack.push(tdApiObject)

            @SuppressLint("SyntheticAccessor")
            when (tdApiObject) {
                is TdApi.Update -> onUpdate(tag, tdApiObject)
                else -> {
                    // TODO: handle this case
                    val message = tdApiObject.toString()

                    instance?.debugLogAndToast(message)

                    assertionFailure()
                }
            }
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

                    val message = tdApiUpdate.toString()

                    instance?.debugLogAndToast(message)

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

                    val message = connectionState.toString()

                    instance?.debugLogAndToast(message)

                    assertionFailure()

                    ""
                }
            }

            val message = "$tag: connection state updated ($state)"

            instance?.debugLogAndToast(message)

            // TODO: show connection state
        }

        internal val updatesExceptionHandler = Client.ExceptionHandler { error ->
            tdErrorsStack.push(error)
            tdObjectsAndErrorsStack.push(error)

            val message = error.localizedMessage

            instance?.debugLogAndToast(message)
        }

        internal val defaultExceptionHandler = Client.ExceptionHandler { error ->
            tdErrorsStack.push(error)
            tdObjectsAndErrorsStack.push(error)

            val message = error.localizedMessage

            instance?.debugLogAndToast(message)
        }

        @SuppressLint("SyntheticAccessor")
        private fun onUpdateAuthorizationState(
            tag: String,
            updateAuthorizationState: TdApi.UpdateAuthorizationState
        ) {
            tdUpdateAuthorizationStateStack.push(updateAuthorizationState)

            when (val authorizationState = updateAuthorizationState.authorizationState) {
                is TdApi.AuthorizationStateWaitTdlibParameters -> {
                    val message = "$tag: TDLib waits parameters"

                    instance?.debugLogAndToast(message)
                }
                is TdApi.AuthorizationStateWaitEncryptionKey -> {
                    val message = "$tag: TDLib waits encryption key (isEncrypted: ${authorizationState.isEncrypted})"

                    instance?.debugLogAndToast(message)
                }
                is TdApi.AuthorizationStateWaitPhoneNumber -> {
                    val message = "$tag: TDLib waits phone number"

                    instance?.debugLogAndToast(message)
                }
                is TdApi.AuthorizationStateWaitCode -> onAuthorizationStateWaitCode(authorizationState, tag)
                else -> {
                    // TODO: handle this case

                    val message = "$tag: $authorizationState"

                    instance?.debugLogAndToast(message)

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

            val message = "$tag: TDLib waits code (isRegistered = $isRegistered; $extraSB)"

            instance?.debugLogAndToast(message)
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
                        val message = "$tag: TdApi.OptionValueEmpty"

                        instance?.debugLogAndToast(message)
                        assertionFailure()
                        ""
                    }
                    else -> {
                        val message = "$tag: Unspecified TdApi.OptionValue"

                        instance?.debugLogAndToast(message)
                        assertionFailure()
                        ""
                    }
                }

            val message = "$tag: $updateOptionName: $updateOptionValue"
            instance?.debugLogAndToast(message)
        }
    }

}
