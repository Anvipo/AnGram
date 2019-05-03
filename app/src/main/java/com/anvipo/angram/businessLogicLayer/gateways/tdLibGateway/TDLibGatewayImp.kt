package com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway

import android.content.Context
import android.os.Build
import com.anvipo.angram.BuildConfig
import kotlinx.coroutines.suspendCancellableCoroutine
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


@Suppress("DirectUseOfResultType")
class TDLibGatewayImp(
    private val tgClient: Client
) : TDLibGateway {

    override suspend fun getAuthStateRequestCatching(): Result<TdApi.Object> =
        suspendCancellableCoroutine { continuation ->
            val getAuthStateQuery = TdApi.GetAuthorizationState()

            val getAuthStateResultHandler = Client.ResultHandler { result ->
                continuation.resume(Result.success(result))
            }

            val getAuthStateExceptionHandler = Client.ExceptionHandler { exception ->
                continuation.resumeWithException(exception)
            }

            tgClient.send(getAuthStateQuery, getAuthStateResultHandler, getAuthStateExceptionHandler)
        }

    override suspend fun setTdLibParametersCatching(context: Context): Result<TdApi.Object> =
        suspendCancellableCoroutine { continuation ->
            val parameters = createTDLibParameters(context)

            val setTdLibParametersResultHandler = Client.ResultHandler { result ->
                continuation.resume(Result.success(result))
            }

            val setTdLibParametersExceptionHandler = Client.ExceptionHandler { exception ->
                continuation.resumeWithException(exception)
            }

            tgClient.send(
                TdApi.SetTdlibParameters(parameters),
                setTdLibParametersResultHandler,
                setTdLibParametersExceptionHandler
            )
        }

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