package com.dnegusoft.mbsmonitoreo.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnegusoft.mbsmonitoreo.constants.DataStoreConstants
import com.dnegusoft.mbsmonitoreo.db.dao.ActividadDao
import com.dnegusoft.mbsmonitoreo.db.dao.MaquinariaDao
import com.dnegusoft.mbsmonitoreo.db.dao.TimeMovDao
import com.dnegusoft.mbsmonitoreo.db.entity.ActividadEntity
import com.dnegusoft.mbsmonitoreo.db.entity.MaquinariaEntity
import com.dnegusoft.mbsmonitoreo.db.entity.TimeMovEntity
import com.dnegusoft.mbsmonitoreo.di.api.DataState
import com.dnegusoft.mbsmonitoreo.di.module.PreferencesDataStore
import com.dnegusoft.mbsmonitoreo.model.ApiResponse
import com.dnegusoft.mbsmonitoreo.model.HomeAction
import com.dnegusoft.mbsmonitoreo.model.HomeState
import com.dnegusoft.mbsmonitoreo.repository.ApiRepository
import com.dnegusoft.mbsmonitoreo.utils.apiCall
import com.dnegusoft.mbsmonitoreo.utils.liveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

class HomeViewModel(
    private val timeMovDao: TimeMovDao,
    private val maquinariaDao: MaquinariaDao,
    private val actividadDao: ActividadDao,
    private val apiRepository: ApiRepository,
    private val dataStore: PreferencesDataStore
) : ViewModel() {

    init {
        viewModelScope.launch {
            chargeAll = maquinariaDao.getAllMaquinaria().isNotEmpty() && actividadDao.getAllActividad().isNotEmpty()
            gettingData = true
        }
    }

    var gettingData by mutableStateOf(false)
    var chargeAll by mutableStateOf(false)

    private val _userName = MutableStateFlow("User")
    val userName = _userName.asStateFlow()

    var state by mutableStateOf(HomeState())
        private set

    private val _activities = liveData<List<ActividadEntity>>()
    val activities get() = _activities

    private val _machines = liveData<List<MaquinariaEntity>>()
    val machines get() = _machines

    private val _response = liveData<ApiResponse>()
    val response get() = _response

    var loading by mutableStateOf(false)

    private val _loadingSending = MutableStateFlow(false)
    val loadingSending = _loadingSending.asStateFlow()

    private val _movements = MutableStateFlow(listOf<TimeMovEntity>())
    val movements = _movements.asStateFlow()

    fun getName(){
        viewModelScope.launch {
            val name = dataStore.getFirstPreference(DataStoreConstants.NAME,"User")
            _userName.value = name
        }
    }

    fun getMovements() {
        viewModelScope.launch {
            val (date, _) = getCurrentDateAndTime()
            val listMovements = timeMovDao.getAllMovement(date)
            _movements.value = listMovements
        }
    }

    fun sendMovementsPending(){
        viewModelScope.launch {
            _loadingSending.value = true
            val listMovements = timeMovDao.getAllMovementPending()
            listMovements.forEach {
                apiCall( { apiRepository.postMovement(it) }, _response)
                delay(1500L)
            }
            _movements.value = listOf()
            _loadingSending.value = false
            getMovements()
        }
    }

    fun fetchData() {
        apiCall( { apiRepository.getActividades() }, _activities)
        apiCall( { apiRepository.getMaquinaria() }, _machines)
    }

    fun fetchDataOffline() {
        viewModelScope.launch {
            _activities.postValue(DataState.Success(actividadDao.getAllActividad()))
            _machines.postValue(DataState.Success(maquinariaDao.getAllMaquinaria()))
        }
    }

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
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDateTime)

        // Time format: HH:mm:ss
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val formattedTime = timeFormat.format(currentDateTime)

        return Pair(formattedDate, formattedTime)
    }

    private fun saveMovement(date: String,hour: String ,machine: String, state: ActivityState, activity: String) {
        viewModelScope.launch {
            val timeMovEntity = TimeMovEntity(
                fecha = date,
                hora = hour,
                maquina = machine,
                tipo = state.name,
                actividad = activity,
                personId = dataStore.getFirstPreference(DataStoreConstants.ID,"")
            )

            val idInsert = timeMovDao.insertTimeMov(timeMovEntity)
            try {
                apiCall( { apiRepository.postMovement(timeMovEntity.apply { id = idInsert }) }, _response)
            } catch (e: Exception){
                println("Error al guardar movimiento en Server -> $e")
            }
        }

        println("Guardando movimiento -> Fecha: $date Hora: $hour , Máquina: $machine, Estado: $state")
    }

    fun updateState(id: Long){
        viewModelScope.launch {
            timeMovDao.UpdateTimeMov(id)
        }
    }

    fun insertAllActivities(list: List<ActividadEntity>) {
        if(list.isNotEmpty())
            viewModelScope.launch {
                actividadDao.deleteAllActividades()
                list.forEach { activity ->
                    actividadDao.insertActividad(activity)
                }
            }
    }

    fun insertAllMaquinaria(list: List<MaquinariaEntity>) {
        if(list.isNotEmpty())
            viewModelScope.launch {
                maquinariaDao.deleteAllMaquinaria()
                list.forEach { maquinaria ->
                    maquinariaDao.insertMaquinaria(maquinaria)
                }
            }
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