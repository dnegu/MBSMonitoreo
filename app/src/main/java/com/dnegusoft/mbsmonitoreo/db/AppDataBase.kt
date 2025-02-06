package com.dnegusoft.mbsmonitoreo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dnegusoft.mbsmonitoreo.db.dao.ActividadDao
import com.dnegusoft.mbsmonitoreo.db.dao.MaquinariaDao
import com.dnegusoft.mbsmonitoreo.db.dao.TimeMovDao
import com.dnegusoft.mbsmonitoreo.db.entity.TimeMovEntity
import com.dnegusoft.mbsmonitoreo.db.entity.MaquinariaEntity
import com.dnegusoft.mbsmonitoreo.db.entity.ActividadEntity


@Database(
    entities = [
        TimeMovEntity::class,
        MaquinariaEntity::class,
        ActividadEntity::class
    ],
    version = 1,
    exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun timeMovDao(): TimeMovDao
    abstract fun maquinariaDao(): MaquinariaDao
    abstract fun actividadDao(): ActividadDao
}