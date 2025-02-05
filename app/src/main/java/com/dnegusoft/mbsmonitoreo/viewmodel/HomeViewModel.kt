package com.dnegusoft.mbsmonitoreo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dnegusoft.mbsmonitoreo.model.HomeAction
import com.dnegusoft.mbsmonitoreo.model.HomeState

class HomeViewModel(

) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set


    fun onAction(action: HomeAction) {
        state = when(action){
            HomeAction.OnBack -> state.copy(
                user = ""
            )
            else -> state
        }
    }
}