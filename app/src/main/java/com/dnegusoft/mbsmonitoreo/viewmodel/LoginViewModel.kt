package com.dnegusoft.mbsmonitoreo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dnegusoft.mbsmonitoreo.model.LoginAction
import com.dnegusoft.mbsmonitoreo.model.LoginState

class LoginViewModel(

) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private fun verifyCredentials() {
        val user = state.user
        val password = state.password
        val isUserValid = user.isNotEmpty()
        val isPasswordValid = password.isNotEmpty()
        val canLogin = isUserValid && isPasswordValid
        state = state.copy(
            canLogin = canLogin
        )
    }

    fun onAction(action: LoginAction) {
        state = when(action){
            is LoginAction.OnUserChange -> {
                state.copy(user = action.user)
            }

            is LoginAction.OnPasswordChange -> {
                state.copy(password = action.password)
            }

            is LoginAction.OnTogglePasswordVisibility -> {
                state.copy(isPasswordVisible = !state.isPasswordVisible)
            }

            is LoginAction.OnLoginClick -> {
                state.copy(isLoggingIn = true)
            }
        }
        verifyCredentials()
    }
}