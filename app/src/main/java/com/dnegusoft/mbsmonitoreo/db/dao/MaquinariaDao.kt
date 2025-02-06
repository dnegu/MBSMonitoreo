package com.dnegusoft.mbsmonitoreo.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dnegusoft.mbsmonitoreo.db.entity.MaquinariaEntity

@Dao
interface MaquinariaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaquinaria(maquinariaEntity: MaquinariaEntity) : Long

    @Query("SELECT * FROM maquinaria ORDER BY id DESC")
    suspend fun getAllMaquinaria(): List<MaquinariaEntity>

    @Query("DELETE FROM maquinaria")
    suspend fun deleteAllMaquinaria()
}