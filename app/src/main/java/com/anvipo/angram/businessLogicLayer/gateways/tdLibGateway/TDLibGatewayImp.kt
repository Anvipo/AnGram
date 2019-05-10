package com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway

import android.content.Context
import android.os.Build
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.errors.TdApiError
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterAuthCode.types.CorrectAuthCodeType
import kotlinx.coroutines.suspendCancellableCoroutine
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import java.util.*
import kotlin.coroutines.resume


@Suppress("DirectUseOfResultType")
class TDLibGatewayImp(
    private val tdClient: Client,
    private val context: Context
) : TDLibGateway {

    override suspend fun getAuthorizationStateRequestCatching(): Result<TdApi.AuthorizationState> =
        suspendCancellableCoroutine { continuation ->
            val getAuthStateQuery = TdApi.GetAuthorizationState()

            val getAuthStateResultHandler = Client.ResultHandler { result ->
                when (result) {
                    is TdApi.AuthorizationState -> continuation.resume(Result.success(result))
                    is TdApi.Error -> {
                        val tdApiError = parseTDApiError(result)

                        continuation.resume(Result.failure(tdApiError))
                    }
                    else -> continuation.resume(Result.failure(TdApiError.Unspecified))
                }
            }

            val getAuthStateExceptionHandler = Client.ExceptionHandler { exception ->
                continuation.resume(Result.failure(exception))
            }

            tdClient.send(getAuthStateQuery, getAuthStateResultHandler, getAuthStateExceptionHandler)
        }

    override suspend fun setTdLibParametersCatching(context: Context): Result<TdApi.Ok> =
        suspendCancellableCoroutine { continuation ->
            val parameters = createTDLibParameters(context)

            val setTdLibParametersResultHandler = Client.ResultHandler { result ->
                when (result) {
                    is TdApi.Ok -> continuation.resume(Result.success(result))
                    is TdApi.Error -> {
                        val tdApiError = parseTDApiError(result)

                        continuation.resume(Result.failure(tdApiError))
                    }
                    else -> continuation.resume(Result.failure(TdApiError.Unspecified))
                }
            }

            val setTdLibParametersExceptionHandler = Client.ExceptionHandler { exception ->
                continuation.resume(Result.failure(exception))
            }

            val setTdlibParametersQuery = TdApi.SetTdlibParameters(parameters)

            tdClient.send(
                setTdlibParametersQuery,
                setTdLibParametersResultHandler,
                setTdLibParametersExceptionHandler
            )
        }

    override suspend fun checkDatabaseEncryptionKeyCatching(): Result<TdApi.Ok> =
        suspendCancellableCoroutine { continuation ->
            val setTdLibParametersResultHandler = Client.ResultHandler { result ->
                when (result) {
                    is TdApi.Ok -> continuation.resume(Result.success(result))
                    is TdApi.Error -> {
                        val tdApiError = parseTDApiError(result)

                        continuation.resume(Result.failure(tdApiError))
                    }
                    else -> continuation.resume(Result.failure(TdApiError.Unspecified))
                }
            }

            val setTdLibParametersExceptionHandler = Client.ExceptionHandler { exception ->
                continuation.resume(Result.failure(exception))
            }

            val checkDatabaseEncryptionKeyQuery = TdApi.CheckDatabaseEncryptionKey()

            tdClient.send(
                checkDatabaseEncryptionKeyQuery,
                setTdLibParametersResultHandler,
                setTdLibParametersExceptionHandler
            )
        }

    override suspend fun setAuthenticationPhoneNumberCatching(enteredPhoneNumber: String): Result<TdApi.Ok> =
        suspendCancellableCoroutine { continuation ->
            val setTdLibParametersResultHandler = Client.ResultHandler { result ->
                when (result) {
                    is TdApi.Ok -> continuation.resume(Result.success(result))
                    is TdApi.Error -> {
                        val tdApiError = parseTDApiError(result)

                        continuation.resume(Result.failure(tdApiError))
                    }
                    else -> continuation.resume(Result.failure(TdApiError.Unspecified))
                }
            }

            val setTdLibParametersExceptionHandler = Client.ExceptionHandler { exception ->
                continuation.resume(Result.failure(exception))
            }

            val setAuthenticationPhoneNumberQuery = TdApi.SetAuthenticationPhoneNumber(
                enteredPhoneNumber,
                false,
                false
            )

            tdClient.send(
                setAuthenticationPhoneNumberQuery,
                setTdLibParametersResultHandler,
                setTdLibParametersExceptionHandler
            )
        }

    override suspend fun checkAuthenticationCodeCatching(enteredAuthCode: CorrectAuthCodeType): Result<TdApi.Ok> =
        suspendCancellableCoroutine { continuation ->
            val checkAuthenticationCodeResultHandler = Client.ResultHandler { result ->
                when (result) {
                    is TdApi.Ok -> continuation.resume(Result.success(result))
                    is TdApi.Error -> {
                        val tdApiError = parseTDApiError(result)

                        continuation.resume(Result.failure(tdApiError))
                    }
                    else -> continuation.resume(Result.failure(TdApiError.Unspecified))
                }
            }

            val checkAuthenticationCodeExceptionHandler = Client.ExceptionHandler { exception ->
                continuation.resume(Result.failure(exception))
            }

            val checkAuthenticationCodeQuery = TdApi.CheckAuthenticationCode(
                enteredAuthCode,
                "",
                ""
            )

            tdClient.send(
                checkAuthenticationCodeQuery,
                checkAuthenticationCodeResultHandler,
                checkAuthenticationCodeExceptionHandler
            )
        }


    /// PRIVATE


    private fun parseTDApiError(result: TdApi.Error): TdApiError =
        when (val errorCode = result.code) {
            TdApiError.Codes.EMPTY_PARAMETER.value ->
                TdApiError.Custom.EmptyParameter(
                    errorCode,
                    result.message
                )
            TdApiError.Codes.BAD_REQUEST.value ->
                TdApiError.Custom.BadRequest(
                    errorCode,
                    result.message
                )
            TdApiError.Codes.DATABASE_ENCRYPTION_KEY_IS_NEEDED.value ->
                TdApiError.Custom.DatabaseEncryptionKeyIsNeeded(
                    errorCode,
                    result.message
                )
            else -> TdApiError.Unspecified
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
        // TODo: use production
        parameters.useTestDc = true
        return parameters
    }

}
