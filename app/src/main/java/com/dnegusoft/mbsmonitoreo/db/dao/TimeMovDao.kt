package com.dnegusoft.mbsmonitoreo.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dnegusoft.mbsmonitoreo.db.entity.TimeMovEntity

@Dao
interface TimeMovDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeMov(timeMov: TimeMovEntity) : Long

    @Query("SELECT * FROM time_movement WHERE fecha = :date ORDER BY id DESC")
    suspend fun getAllMovement(date: String): List<TimeMovEntity>

    @Query("UPDAte  time_movement SET status = 'F' WHERE id = :id")
    suspend fun UpdateTimeMov(id: Long)
}