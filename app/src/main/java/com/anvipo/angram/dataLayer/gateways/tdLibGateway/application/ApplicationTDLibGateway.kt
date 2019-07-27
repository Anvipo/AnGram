package com.anvipo.angram.dataLayer.gateways.tdLibGateway.application

import android.content.Context
import com.anvipo.angram.dataLayer.gateways.tdLibGateway.base.BaseTdLibGateway
import org.drinkless.td.libcore.telegram.TdApi

interface ApplicationTDLibGateway : BaseTdLibGateway {

    suspend fun setTdLibParametersCatching(context: Context): Result<TdApi.Ok>

    suspend fun checkDatabaseEncryptionKeyCatching(): Result<TdApi.Ok>

    suspend fun logoutCatching(): Result<TdApi.Ok>

}