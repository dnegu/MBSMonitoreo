package com.dnegusoft.mbsmonitoreo.di.api

import com.dnegusoft.mbsmonitoreo.db.entity.ActividadEntity
import com.dnegusoft.mbsmonitoreo.db.entity.MaquinariaEntity
import com.dnegusoft.mbsmonitoreo.db.entity.TimeMovEntity
import com.dnegusoft.mbsmonitoreo.model.ApiResponse
import com.dnegusoft.mbsmonitoreo.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("actividades.php")
    suspend fun actividades() : Response<List<ActividadEntity>>

    @GET("maquinas.php")
    suspend fun maquinaria() : Response<List<MaquinariaEntity>>

    @POST("movimientos.php")
    suspend fun insertarMovimiento(@Body movimiento: TimeMovEntity): Response<ApiResponse>

    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
        @Field("usuario") usuario: String,
        @Field("contrasena") contrasena: String
    ): Response<LoginResponse>
}