package com.dnegusoft.mbsmonitoreo.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnegusoft.mbsmonitoreo.db.dao.TimeMovDao
import com.dnegusoft.mbsmonitoreo.db.entity.TimeMovEntity
import com.dnegusoft.mbsmonitoreo.model.HomeAction
import com.dnegusoft.mbsmonitoreo.model.HomeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

class HomeViewModel(
    private val timeMovDao: TimeMovDao
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

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    val machines = listOf("Cargador frontal 1", "Cargador frontal 2", "Cargador frontal 3", "Montacarga 1")

    val activities = listOf("Tiempo muerto", "Carga de material", "Descarga de material", "Tiempo de espera")

    fun startClock() {
        viewModelScope.launch {
            while (true) {
                _uiState.value = _uiState.value.copy(
                    currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                )
                delay(1000)
            }
        }
    }

    fun toggleDropdown() {
        _uiState.value = _uiState.value.copy(
            isDropdownExpanded = !_uiState.value.isDropdownExpanded
        )
    }

    fun toggleDropdownActivity() {
        _uiState.value = _uiState.value.copy(
            isActivityDropdownExpanded = !_uiState.value.isActivityDropdownExpanded
        )
    }

    fun selectMachine(machine: String) {
        _uiState.value = _uiState.value.copy(
            selectedMachine = machine,
            isDropdownExpanded = false
        )
    }

    fun selectActivity(activity: String) {
        _uiState.value = _uiState.value.copy(
            selectedActivity = activity,
            isActivityDropdownExpanded = false
        )
    }

    fun confirmMachineSelection() {
        _uiState.value = _uiState.value.copy(isMachineSelected = true)
    }

    fun confirmActivitySelection(state : Boolean = true) {
        _uiState.value = _uiState.value.copy(isActivitySelected = state)
    }

    fun toggleActivity() {
        val (date, hour ) = getCurrentDateAndTime()

        val newState = if (_uiState.value.activityState == ActivityState.FIN) {
            ActivityState.INICIO
        } else {
            ActivityState.FIN
        }

        saveMovement(
            hour = hour,
            date = date,
            machine = _uiState.value.selectedMachine ?: "",
            state = _uiState.value.activityState,
            activity = _uiState.value.selectedActivity ?: ""
        )

        _uiState.value = _uiState.value.copy(activityState = newState)
    }

    fun getCurrentDateAndTime(): Pair<String, String> {
        val currentDateTime = Date()

        // Date format: DD/MM/YYYY
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDateTime)

        // Time format: HH:mm:ss
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val formattedTime = timeFormat.format(currentDateTime)

        return Pair(formattedDate, formattedTime)
    }

    private fun saveMovement(date: String,hour: String ,machine: String, state: ActivityState, activity: String) {
        viewModelScope.launch {
            timeMovDao.insertTimeMov(
                TimeMovEntity(
                    fecha = date,
                    hora = hour,
                    maquina = machine,
                    tipo = state.name,
                    actividad = activity
                )
            )
        }

        println("Guardando movimiento -> Fecha: $date Hora: $hour , Máquina: $machine, Estado: $state")
    }
}


data class UiState(
    val currentTime: String = "",
    val selectedMachine: String? = null,
    val selectedActivity: String? = null,
    val isDropdownExpanded: Boolean = false,
    val isMachineSelected: Boolean = false,
    val isActivitySelected: Boolean = false,
    val isActivityDropdownExpanded: Boolean = false,
    val activityState: ActivityState = ActivityState.INICIO
)

enum class ActivityState {
    INICIO, FIN
}