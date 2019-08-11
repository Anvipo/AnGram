package com.anvipo.angram.layers.data.gateways.tdLib.application

import android.os.Build
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.data.gateways.tdLib.base.BaseTdLibGatewayImp
import com.anvipo.angram.layers.global.GlobalHelpers.USE_TEST_ENVIRONMENT
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import java.util.*

class ApplicationTDLibGatewayImp(
    tdClient: Client,
    private val resourceManager: ResourceManager
) :
    BaseTdLibGatewayImp(tdClient),
    ApplicationTDLibGateway {

    override suspend fun setTdLibParametersCatching(): Result<TdApi.Ok> =
        doRequestCatching(TdApi.SetTdlibParameters(createTDLibParameters()))

    override suspend fun checkDatabaseEncryptionKeyCatching(): Result<TdApi.Ok> =
        doRequestCatching(TdApi.CheckDatabaseEncryptionKey())

    override suspend fun logoutCatching(): Result<TdApi.Ok> =
        doRequestCatching(TdApi.LogOut())


    private fun createTDLibParameters(): TdApi.TdlibParameters {
        val parameters = TdApi.TdlibParameters()
        val dbDirectory = resourceManager.context.filesDir?.absolutePath

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
        parameters.useTestDc = USE_TEST_ENVIRONMENT

        return parameters
    }

}