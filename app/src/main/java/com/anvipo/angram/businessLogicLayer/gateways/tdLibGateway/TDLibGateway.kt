package com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway

import android.content.Context
import org.drinkless.td.libcore.telegram.TdApi

@Suppress("DirectUseOfResultType")
interface TDLibGateway {

    suspend fun getAuthStateRequestCatching(): Result<TdApi.Object>

    suspend fun setTdLibParametersCatching(context: Context): Result<TdApi.Object>

}
