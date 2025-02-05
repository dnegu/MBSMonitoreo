package com.dnegusoft.mbsmonitoreo.di.api

import com.dnegusoft.mbsmonitoreo.model.BaseModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/fact")
    suspend fun data() : Response<BaseModel>
}