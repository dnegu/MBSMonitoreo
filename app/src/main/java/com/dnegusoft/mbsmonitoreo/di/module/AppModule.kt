package com.dnegusoft.mbsmonitoreo.di.module

import com.dnegusoft.mbsmonitoreo.repository.ApiRepository
import com.dnegusoft.mbsmonitoreo.viewmodel.ApiViewModel
import com.dnegusoft.mbsmonitoreo.viewmodel.HomeViewModel
import com.dnegusoft.mbsmonitoreo.viewmodel.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        ApiRepository(get())
    }
    viewModel { ApiViewModel(get()) }
    viewModel { LoginViewModel() }
    viewModel { HomeViewModel() }
}