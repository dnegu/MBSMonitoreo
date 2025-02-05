package com.dnegusoft.mbsmonitoreo.model

data class LoginState(
    val user: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val canLogin: Boolean = false,
    val isLoggingIn: Boolean = false
)