package com.dnegusoft.mbsmonitoreo.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "maquinaria")
data class MaquinariaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val nombre: String,
    val tipo: String
)