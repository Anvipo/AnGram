package com.anvipo.angram.dataLayer.gateways.localGateway.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anvipo.angram.dataLayer.gateways.localGateway.db.room.entities.proxy.TdApiProxyInfoRoomEntity
import com.anvipo.angram.dataLayer.gateways.localGateway.db.room.proxy.ProxyRoomDAO

@Suppress("unused")
@Database(
    entities = [TdApiProxyInfoRoomEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TdApiProxyInfoRoomEntity.Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val proxyDao: ProxyRoomDAO

}