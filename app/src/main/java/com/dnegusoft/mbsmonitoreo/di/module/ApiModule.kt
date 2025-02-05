package com.dnegusoft.mbsmonitoreo.di.module

import com.dnegusoft.mbsmonitoreo.constants.Constants
import com.dnegusoft.mbsmonitoreo.di.api.ApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    prettyPrint = true
}

val apiModule = module {

    single {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            ).build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(get())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single {
        get <Retrofit>().create(ApiService::class.java)
    }
}