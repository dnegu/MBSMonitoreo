package com.dnegusoft.mbsmonitoreo.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dnegusoft.mbsmonitoreo.db.entity.ActividadEntity

@Dao
interface ActividadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActividad(maquinariaEntity: ActividadEntity) : Long

    @Query("SELECT * FROM actividades ORDER BY id DESC")
    suspend fun getAllActividad(): List<ActividadEntity>

    @Query("DELETE FROM actividades")
    suspend fun deleteAllActividades()
}