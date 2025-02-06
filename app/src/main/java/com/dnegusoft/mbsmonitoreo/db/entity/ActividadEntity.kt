package com.dnegusoft.mbsmonitoreo.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "actividades")
data class ActividadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nombre: String,
    val tipo: String
)