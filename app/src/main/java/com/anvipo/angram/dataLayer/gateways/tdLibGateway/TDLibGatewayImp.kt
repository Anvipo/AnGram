package com.anvipo.angram.dataLayer.gateways.tdLibGateway

import android.content.Context
import android.os.Build
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.base.BaseTdLibGateway
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationCode.types.CorrectAuthenticationCodeType
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthenticationPassword.types.CorrectAuthenticationPasswordType
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import java.util.*

class TDLibGatewayImp(tdClient: Client) : BaseTdLibGateway(tdClient), TDLibGateway {

    override suspend fun getAuthorizationStateRequestCatching(): Result<TdApi.AuthorizationState> =
        doRequestCatching(TdApi.GetAuthorizationState())

    override suspend fun setTdLibParametersCatching(context: Context): Result<TdApi.Ok> =
        doRequestCatching(TdApi.SetTdlibParameters(createTDLibParameters(context)))

    override suspend fun checkDatabaseEncryptionKeyCatching(): Result<TdApi.Ok> =
        doRequestCatching(TdApi.CheckDatabaseEncryptionKey())

    override suspend fun setAuthenticationPhoneNumberCatching(enteredPhoneNumber: String): Result<TdApi.Ok> =
        doRequestCatching(
            TdApi.SetAuthenticationPhoneNumber(
                enteredPhoneNumber,
                true,
                true
            )
        )

    override suspend fun resendAuthenticationCodeCatching(): Result<TdApi.Ok> =
        doRequestCatching(TdApi.ResendAuthenticationCode())

    override suspend fun checkAuthenticationCodeCatching(
        enteredAuthenticationCode: CorrectAuthenticationCodeType,
        lastName: String,
        firstName: String
    ): Result<TdApi.Ok> =
        doRequestCatching(
            TdApi.CheckAuthenticationCode(
                enteredAuthenticationCode,
                firstName,
                lastName
            )
        )

    override suspend fun checkAuthenticationPasswordCatching(
        enteredAuthenticationPassword: CorrectAuthenticationPasswordType
    ): Result<TdApi.Ok> =
        doRequestCatching(TdApi.CheckAuthenticationPassword(enteredAuthenticationPassword))

    override suspend fun logoutCatching(): Result<TdApi.Ok> =
        doRequestCatching(TdApi.LogOut())

    /// PRIVATE


    private fun createTDLibParameters(context: Context): TdApi.TdlibParameters {
        val parameters = TdApi.TdlibParameters()

        val dbDirectory = context.filesDir?.absolutePath

        parameters.apiHash = "dca2591dafcacf1a23908a256f1b6711"
        parameters.apiId = 848516

        val applicationVersion = BuildConfig.VERSION_NAME

        parameters.applicationVersion = applicationVersion
        parameters.databaseDirectory = dbDirectory

        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL

        val deviceModel = "$manufacturer $model"

        parameters.deviceModel = deviceModel
        parameters.enableStorageOptimizer = true
        parameters.filesDirectory = dbDirectory
        parameters.ignoreFileNames = true

        val systemLanguage = Locale.getDefault().language
        val systemCountry = Locale.getDefault().country

        parameters.systemLanguageCode = "$systemLanguage-$systemCountry"

        val systemVersion = "Android " + Build.VERSION.RELEASE + " (${Build.VERSION.SDK_INT})"

        parameters.systemVersion = systemVersion
        parameters.useChatInfoDatabase = true
        parameters.useFileDatabase = true
        parameters.useMessageDatabase = true
        parameters.useSecretChats = false
        parameters.useTestDc = true
        return parameters
    }

}
