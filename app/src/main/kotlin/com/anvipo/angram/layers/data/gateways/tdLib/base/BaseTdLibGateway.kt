package com.anvipo.angram.layers.data.gateways.tdLib.base

import org.drinkless.td.libcore.telegram.TdApi

interface BaseTdLibGateway {

    suspend fun setNetworkTypeCatching(type: TdApi.NetworkType): Result<TdApi.Ok>

}