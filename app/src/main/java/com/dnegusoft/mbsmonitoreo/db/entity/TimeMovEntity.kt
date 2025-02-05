package com.dnegusoft.mbsmonitoreo.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "time_movement")
data class TimeMovEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val actividad: String,
    val fecha: String,
    val hora: String,
    val tipo: String,
    val maquina: String
)