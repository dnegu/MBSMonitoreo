package com.dnegusoft.mbsmonitoreo.viewmodel

import androidx.lifecycle.ViewModel
import com.dnegusoft.mbsmonitoreo.model.BaseModel
import com.dnegusoft.mbsmonitoreo.repository.ApiRepository
import com.dnegusoft.mbsmonitoreo.utils.apiCall
import com.dnegusoft.mbsmonitoreo.utils.liveData

class ApiViewModel(private val apiRepository: ApiRepository) : ViewModel() {
    private val _data = liveData<BaseModel>()
    val data get() = _data

    fun fetchData() {
        apiCall( { apiRepository.getData() }, _data)
    }

}