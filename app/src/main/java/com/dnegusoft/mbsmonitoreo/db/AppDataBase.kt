package com.dnegusoft.mbsmonitoreo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dnegusoft.mbsmonitoreo.db.dao.TimeMovDao
import com.dnegusoft.mbsmonitoreo.db.entity.TimeMovEntity


@Database(
    entities = [
        TimeMovEntity::class
    ],
    version = 1,
    exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun timeMovDao(): TimeMovDao
}