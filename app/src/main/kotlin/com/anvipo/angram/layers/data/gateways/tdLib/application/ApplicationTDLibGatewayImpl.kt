package com.anvipo.angram.layers.data.gateways.tdLib.application

import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.layers.core.NetworkConnectionState
import com.anvipo.angram.layers.core.ResourceManager
import com.anvipo.angram.layers.data.gateways.tdLib.base.BaseTdLibGateway
import com.anvipo.angram.layers.global.GlobalHelpers.USE_TEST_ENVIRONMENT
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import java.util.*

class ApplicationTDLibGatewayImpl(
    tdLibClient: Client,
    private val resourceManager: ResourceManager
) :
    BaseTdLibGateway(tdLibClient),
    ApplicationTDLibGateway {

    override suspend fun onChangeNetworkConnectionState(
        newState: NetworkConnectionState
    ): Result<TdApi.Ok> {
        val networkType: TdApi.NetworkType =
            if (newState.isAvailable) {
                when (newState.connectionState) {
                    null -> TdApi.NetworkTypeOther()
                    TRANSPORT_CELLULAR -> TdApi.NetworkTypeMobile()
                    TRANSPORT_WIFI -> TdApi.NetworkTypeWiFi()
                    else -> TdApi.NetworkTypeOther()
                }
            } else {
                TdApi.NetworkTypeNone()
            }

        return doRequestCatching(TdApi.SetNetworkType(networkType))
    }

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