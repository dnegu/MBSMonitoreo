package com.dnegusoft.mbsmonitoreo.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "time_movement")
data class TimeMovEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    val actividad: String,
    val fecha: String,
    val hora: String,
    val tipo: String,
    val maquina: String,
    val personId: String,
    val status: String = "P"
)