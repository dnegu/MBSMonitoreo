package com.dnegusoft.mbsmonitoreo.di.module

import android.content.Context
import androidx.room.Room
import com.dnegusoft.mbsmonitoreo.db.AppDataBase
import com.dnegusoft.mbsmonitoreo.repository.ApiRepository
import com.dnegusoft.mbsmonitoreo.viewmodel.HomeViewModel
import com.dnegusoft.mbsmonitoreo.viewmodel.LoginViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    //Database
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDataBase::class.java,
            "mbs_monitoreo"
        ).build()
    }

    //Repository
    single { ApiRepository(get())}

    //Dao
    single { get<AppDataBase>().timeMovDao() }
    single { get<AppDataBase>().maquinariaDao() }
    single { get<AppDataBase>().actividadDao() }

    //DataSore
    single {
        createDataStore { (get() as Context).filesDir.resolve(dataStoreFileName).absolutePath }
    }

    singleOf(::PreferencesDataStoreImpl).bind<PreferencesDataStore>()

    //ViewModels
    viewModel { LoginViewModel(get(),get()) }
    viewModel { HomeViewModel(get(),get(),get(),get()) }
}