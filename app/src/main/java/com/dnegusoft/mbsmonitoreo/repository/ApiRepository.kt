package com.dnegusoft.mbsmonitoreo.repository

import com.dnegusoft.mbsmonitoreo.db.entity.ActividadEntity
import com.dnegusoft.mbsmonitoreo.db.entity.MaquinariaEntity
import com.dnegusoft.mbsmonitoreo.db.entity.TimeMovEntity
import com.dnegusoft.mbsmonitoreo.di.api.ApiService
import com.dnegusoft.mbsmonitoreo.di.api.DataState
import com.dnegusoft.mbsmonitoreo.model.ApiResponse
import com.dnegusoft.mbsmonitoreo.model.LoginResponse
import com.dnegusoft.mbsmonitoreo.utils.safeCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ApiRepository(private val apiService: ApiService) {

    suspend fun getActividades(): Flow<DataState<List<ActividadEntity>>> {
        return try {
            safeCall { apiService.actividades() }
        } catch (e: Exception){
            flow { emit(DataState.Success(listOf()))}
        }
    }

    suspend fun getMaquinaria(): Flow<DataState<List<MaquinariaEntity>>> {
        return try {
            safeCall { apiService.maquinaria() }
        } catch (e: Exception){
            flow { emit(DataState.Success(listOf()))}
        }
    }

    suspend fun postMovement(movEntity: TimeMovEntity): Flow<DataState<ApiResponse>> {
        return try {
            safeCall { apiService.insertarMovimiento(movEntity) }
        } catch (e: Exception) {
            flow { emit(DataState.Success(ApiResponse()))}
        }
    }

    suspend fun login(user: String, password: String): Flow<DataState<LoginResponse>> {
        return try {
            safeCall { apiService.login(user, password) }
        } catch (e: Exception) {
            flow { emit(DataState.Success(LoginResponse()))}
        }
    }
}