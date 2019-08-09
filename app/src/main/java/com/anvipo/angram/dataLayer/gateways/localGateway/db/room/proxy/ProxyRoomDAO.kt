package com.anvipo.angram.dataLayer.gateways.localGateway.db.room.proxy

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Query
import com.anvipo.angram.dataLayer.gateways.localGateway.db.room.base.BaseRoomDAO
import com.anvipo.angram.dataLayer.gateways.localGateway.db.room.entities.proxy.TdApiProxyInfoRoomEntity
import com.anvipo.angram.dataLayer.gateways.localGateway.db.room.entities.proxy.TdApiProxyInfoRoomEntity.Companion.TD_API_PROXY_INFOS_TABLE_NAME

@Suppress("unused")
@WorkerThread
@Dao
interface ProxyRoomDAO : BaseRoomDAO<TdApiProxyInfoRoomEntity> {

    @Query("SELECT * FROM $TD_API_PROXY_INFOS_TABLE_NAME")
    suspend fun allSavedProxyInfos(): List<TdApiProxyInfoRoomEntity>

}