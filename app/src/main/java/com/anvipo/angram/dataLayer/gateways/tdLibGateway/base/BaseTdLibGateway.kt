package com.anvipo.angram.dataLayer.gateways.tdLibGateway.base

import com.anvipo.angram.dataLayer.gateways.tdLibGateway.errors.TdApiError
import kotlinx.coroutines.suspendCancellableCoroutine
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import kotlin.coroutines.resume

abstract class BaseTdLibGateway(
    protected val tdClient: Client
) {

    protected suspend inline fun <reified ResultType : TdApi.Object> doRequestCatching(
        query: TdApi.Function
    ): Result<ResultType> =
        suspendCancellableCoroutine { continuation ->
            val resultHandler = Client.ResultHandler { result ->
                when (result) {
                    is ResultType -> continuation.resume(Result.success(result))
                    is TdApi.Error -> {
                        val tdApiError = parseTDApiError(result)

                        continuation.resume(Result.failure(tdApiError))
                    }
                    else -> continuation.resume(Result.failure(TdApiError.Unspecified))
                }
            }

            val exceptionHandler = Client.ExceptionHandler { exception ->
                continuation.resume(Result.failure(exception))
            }

            tdClient.send(
                query,
                resultHandler,
                exceptionHandler
            )
        }

    protected fun parseTDApiError(result: TdApi.Error): TdApiError =
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
            TdApiError.Codes.TOO_MANY_REQUESTS.value ->
                TdApiError.Custom.TooManyRequests(
                    errorCode,
                    result.message
                )
            else -> TdApiError.Unspecified
        }

}