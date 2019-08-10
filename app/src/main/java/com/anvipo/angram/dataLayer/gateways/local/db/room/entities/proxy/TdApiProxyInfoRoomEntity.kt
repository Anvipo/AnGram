package com.anvipo.angram.dataLayer.gateways.local.db.room.entities.proxy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.anvipo.angram.coreLayer.CoreHelpers.assertionFailure
import com.anvipo.angram.dataLayer.gateways.local.db.room.base.BaseConverter
import com.anvipo.angram.dataLayer.gateways.local.db.room.entities.proxy.TdApiProxyInfoRoomEntity.Companion.TD_API_PROXY_INFOS_TABLE_NAME
import com.anvipo.angram.dataLayer.gateways.local.db.room.entities.proxy.type.base.TdApiProxyTypeRoomEntity
import com.anvipo.angram.dataLayer.gateways.local.db.room.entities.proxy.type.imp.TdApiMTProtoProxyTypeRoomEntity

@Entity(tableName = TD_API_PROXY_INFOS_TABLE_NAME)
class TdApiProxyInfoRoomEntity(

    @PrimaryKey
    @ColumnInfo(name = PROXY_ID_PROPERTY_NAME)
    val proxyID: Int,

    @ColumnInfo(name = SERVER_PROPERTY_NAME)
    val server: String,

    @ColumnInfo(name = PORT_PROPERTY_NAME)
    val port: Int,

    @ColumnInfo(name = LAST_USED_DATE_PROPERTY_NAME)
    val lastUsedDate: Int,

    @ColumnInfo(name = IS_ENABLED_PROPERTY_NAME)
    val isEnabled: Boolean,

    val type: TdApiProxyTypeRoomEntity

) {

    companion object {

        const val TD_API_PROXY_INFOS_TABLE_NAME: String = "td_api_proxy_infos"

        const val PROXY_ID_PROPERTY_NAME: String = "proxy_id"
        const val SERVER_PROPERTY_NAME: String = "server"
        const val PORT_PROPERTY_NAME: String = "port"
        const val LAST_USED_DATE_PROPERTY_NAME: String = "last_used_date"
        const val IS_ENABLED_PROPERTY_NAME: String = "is_enabled"

    }

    @Suppress("unused")
    class Converters : BaseConverter() {

        @TypeConverter
        fun fromTdApiProxyTypeRoomEntity(tdApiProxyTypeRoomEntity: TdApiProxyTypeRoomEntity): String {
            val clazz: Class<*> = when (tdApiProxyTypeRoomEntity) {
                is TdApiMTProtoProxyTypeRoomEntity -> {
                    TdApiMTProtoProxyTypeRoomEntity::class.java
                }
                else -> TODO()
            }

            return transformClassAndHisFieldsWithTheirValuesToString(clazz, tdApiProxyTypeRoomEntity)
        }


        @TypeConverter
        fun fromString(string: String): TdApiProxyTypeRoomEntity {
            val (className, fieldsAndTheirValues) = getClassNameAndFieldsAndTheirValuesPairFrom(string)

            val mtProtoProxyTypeClazz = TdApiMTProtoProxyTypeRoomEntity::class.java
            when (className) {
                mtProtoProxyTypeClazz.simpleName -> {
                    var secretValue = ""

                    for (fieldWithValueString in fieldsAndTheirValues) {
                        val (fieldName, fieldValue) = split(fieldWithValueString)

                        if (fieldName == TdApiMTProtoProxyTypeRoomEntity::secret.name) {
                            secretValue = fieldValue
                        } else {
                            assertionFailure()
                        }
                    }

                    return TdApiMTProtoProxyTypeRoomEntity(secretValue)
                }
                else -> TODO()
            }
        }

    }

}

