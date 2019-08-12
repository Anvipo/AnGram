package com.anvipo.angram.layers.data.gateways.tdLib.base

import com.anvipo.angram.layers.data.gateways.tdLib.errors.TdApiError
import kotlinx.coroutines.suspendCancellableCoroutine
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import kotlin.coroutines.resume

abstract class BaseTdLibGateway(
    protected val tdLibClient: Client
) {

    protected suspend inline fun <reified ResultType : TdApi.Object> doRequestCatching(
        query: TdApi.Function
    ): Result<ResultType> =
        suspendCancellableCoroutine { continuation ->
            val resultHandler = Client.ResultHandler {
                when (it) {
                    is ResultType -> continuation.resume(Result.success(it))
                    is TdApi.Error -> continuation.resume(
                        Result.failure(
                            TdApiError(message = it.message, code = it.code)
                        )
                    )
                    else -> continuation.resume(Result.failure(TdApiError.Unspecified))
                }
            }

            val exceptionHandler = Client.ExceptionHandler { exception ->
                continuation.resume(Result.failure(exception))
            }

            tdLibClient.send(
                query,
                resultHandler,
                exceptionHandler
            )
        }

}