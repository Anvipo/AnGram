package com.anvipo.angram.layers.data.gateways.local.db.room.base

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
    suspend fun insertOrReplace(obj: T): Long

    @Insert(onConflict = ABORT)
    suspend fun insertOrAbortTransaction(obj: T): Long

    @Insert(onConflict = IGNORE)
    suspend fun insertOrIgnoreConflict(obj: T): Long


    @Insert(onConflict = REPLACE)
    suspend fun insertAllOrReplace(vararg objs: T): LongArray

    @Insert(onConflict = ABORT)
    suspend fun insertAllOrAbortTransaction(vararg objs: T): LongArray

    @Insert(onConflict = IGNORE)
    suspend fun insertAllOrIgnoreConflict(vararg objs: T): LongArray


    @Update
    suspend fun update(vararg obj: T): Int

    @Delete
    suspend fun delete(vararg obj: T): Int

}