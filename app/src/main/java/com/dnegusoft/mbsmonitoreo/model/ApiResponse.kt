package com.dnegusoft.mbsmonitoreo.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val success: String? = "",
    val error: String? = "",
    val id: Long? = 0,
)