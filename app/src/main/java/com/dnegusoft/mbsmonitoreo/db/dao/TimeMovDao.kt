package com.dnegusoft.mbsmonitoreo.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dnegusoft.mbsmonitoreo.db.entity.TimeMovEntity

@Dao
interface TimeMovDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeMov(timeMov: TimeMovEntity)

    @Query("SELECT * FROM time_movement ORDER BY id DESC")
    suspend fun getAllFacts(): List<TimeMovEntity>
}