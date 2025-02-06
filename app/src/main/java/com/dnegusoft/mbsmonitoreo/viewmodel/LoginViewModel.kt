package com.dnegusoft.mbsmonitoreo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnegusoft.mbsmonitoreo.constants.DataStoreConstants
import com.dnegusoft.mbsmonitoreo.di.module.PreferencesDataStore
import com.dnegusoft.mbsmonitoreo.model.LoginAction
import com.dnegusoft.mbsmonitoreo.model.LoginResponse
import com.dnegusoft.mbsmonitoreo.model.LoginState
import com.dnegusoft.mbsmonitoreo.repository.ApiRepository
import com.dnegusoft.mbsmonitoreo.utils.apiCall
import com.dnegusoft.mbsmonitoreo.utils.liveData
import kotlinx.coroutines.launch

class LoginViewModel(
    private val apiRepository: ApiRepository,
    private val dataStore: PreferencesDataStore
) : ViewModel() {

    init {
        viewModelScope.launch {
            val ID = dataStore.getFirstPreference(DataStoreConstants.ID,"")
            if(ID.isNotEmpty())
                isLoginAfter = true
        }
    }

    var isLoginAfter by mutableStateOf(false)

    var state by mutableStateOf(LoginState())
        private set

    private val _login = liveData<LoginResponse>()
    val login get() = _login

    private fun verifyCredentials() {
        val user = state.user
        val password = state.password
        val isUserValid = user.isNotEmpty()
        val isPasswordValid = password.isNotEmpty()
        val canLogin = isUserValid && user.length > 5 && isPasswordValid && password.length > 5
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
            is LoginAction.OnLoginResponse -> {
                state.copy(isLoggingIn = false)
            }
        }
        verifyCredentials()
    }

    fun loginClick(){
        apiCall( { apiRepository.login(state.user, state.password) }, _login)
    }

    fun saveData(user: String, data: String) {
        viewModelScope.launch {
            dataStore.putPreference(DataStoreConstants.ID, user)
            dataStore.putPreference(DataStoreConstants.NAME, data)
        }

    }
}