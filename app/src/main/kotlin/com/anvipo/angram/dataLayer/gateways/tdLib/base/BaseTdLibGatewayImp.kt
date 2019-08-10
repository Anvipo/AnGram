package com.anvipo.angram.dataLayer.gateways.tdLib.base

import com.anvipo.angram.dataLayer.gateways.tdLib.errors.TdApiError
import kotlinx.coroutines.suspendCancellableCoroutine
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import kotlin.coroutines.resume

abstract class BaseTdLibGatewayImp(
    protected val tdClient: Client
) : BaseTdLibGateway {

    override suspend fun getAuthorizationStateRequestCatching(): Result<TdApi.AuthorizationState> =
        doRequestCatching(TdApi.GetAuthorizationState())

    override suspend fun setNetworkTypeCatching(type: TdApi.NetworkType): Result<TdApi.Ok> =
        doRequestCatching(
            TdApi.SetNetworkType(
                type
            )
        )


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

            tdClient.send(
                query,
                resultHandler,
                exceptionHandler
            )
        }

}