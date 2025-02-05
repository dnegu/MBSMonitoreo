package com.dnegusoft.mbsmonitoreo.di.module

import androidx.room.Room
import com.dnegusoft.mbsmonitoreo.db.AppDataBase
import com.dnegusoft.mbsmonitoreo.repository.ApiRepository
import com.dnegusoft.mbsmonitoreo.viewmodel.ApiViewModel
import com.dnegusoft.mbsmonitoreo.viewmodel.HomeViewModel
import com.dnegusoft.mbsmonitoreo.viewmodel.LoginViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
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

    //ViewModels
    viewModel { ApiViewModel(get()) }
    viewModel { LoginViewModel() }
    viewModel { HomeViewModel(get()) }
}