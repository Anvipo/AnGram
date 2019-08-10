package com.anvipo.angram.businessLogicLayer.useCases.authFlow.addProxyUseCase

import com.anvipo.angram.dataLayer.gateways.local.db.room.entities.proxy.TdApiProxyInfoRoomEntity
import com.anvipo.angram.dataLayer.gateways.local.db.room.entities.proxy.type.base.TdApiProxyTypeRoomEntity
import com.anvipo.angram.dataLayer.gateways.local.db.room.entities.proxy.type.imp.TdApiMTProtoProxyTypeRoomEntity
import com.anvipo.angram.dataLayer.gateways.local.db.room.proxy.ProxyRoomDAO
import com.anvipo.angram.dataLayer.gateways.tdLib.proxy.ProxyTDLibGateway
import org.drinkless.td.libcore.telegram.TdApi

class AddProxyUseCaseImp(
    private val tdLibGateway: ProxyTDLibGateway,
    private val dbGateway: ProxyRoomDAO
) : AddProxyUseCase {

    override suspend fun addProxyCatching(
        server: String,
        port: Int,
        type: TdApi.ProxyType
    ): Result<Unit> =
        tdLibGateway
            .addProxyCatching(
                server = server,
                port = port,
                enable = true,
                type = type
            )
            .map { tdApiProxyInfo ->
                saveProxyInfo(tdApiProxyInfo)

                return@map
            }

    private suspend fun saveProxyInfo(proxyInfo: TdApi.Proxy) {
        val tdApiProxyTypeRoomEntity: TdApiProxyTypeRoomEntity =
            when (val type = proxyInfo.type) {
                is TdApi.ProxyTypeMtproto -> {
                    TdApiMTProtoProxyTypeRoomEntity(
                        secret = type.secret
                    )
                }
                else -> TODO()
            }

        val proxyInfoRoomEntity = TdApiProxyInfoRoomEntity(
            proxyID = proxyInfo.id,
            server = proxyInfo.server,
            port = proxyInfo.port,
            lastUsedDate = proxyInfo.lastUsedDate,
            isEnabled = proxyInfo.isEnabled,
            type = tdApiProxyTypeRoomEntity
        )

        dbGateway.insertAllOrReplace(proxyInfoRoomEntity)
    }

}