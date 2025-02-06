package com.dnegusoft.mbsmonitoreo.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val isSuccess: Boolean? = false,
    val message: String? = "",
    val data: String? = ""
)