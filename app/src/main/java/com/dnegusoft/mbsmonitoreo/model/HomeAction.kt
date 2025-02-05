package com.dnegusoft.mbsmonitoreo.model

sealed interface HomeAction {
    data object OnBack: HomeAction
}