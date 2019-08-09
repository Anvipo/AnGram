package com.anvipo.angram.dataLayer.gateways.localGateway.db.room.base

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.*
import androidx.room.Update

@Suppress("unused")
@WorkerThread
@Dao
interface BaseRoomDAO<in T> {

    @Insert(onConflict = REPLACE)
    suspend fun insertAllOrReplace(vararg obj: T): LongArray

    @Insert(onConflict = ABORT)
    suspend fun insertAllOrAbortTransaction(vararg obj: T): LongArray

    @Insert(onConflict = IGNORE)
    suspend fun insertAllOrIgnoreConflict(vararg obj: T): LongArray


    @Update
    suspend fun update(vararg obj: T): Int

    @Delete
    suspend fun delete(vararg obj: T): Int

}