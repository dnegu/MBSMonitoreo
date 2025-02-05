package com.dnegusoft.mbsmonitoreo.repository

import com.dnegusoft.mbsmonitoreo.di.api.ApiService
import com.dnegusoft.mbsmonitoreo.di.api.DataState
import com.dnegusoft.mbsmonitoreo.model.BaseModel
import com.dnegusoft.mbsmonitoreo.utils.safeCall
import kotlinx.coroutines.flow.Flow

class ApiRepository(private val apiService: ApiService) {

    suspend fun getData(): Flow<DataState<BaseModel>> {
        return safeCall { apiService.data() }
    }

}