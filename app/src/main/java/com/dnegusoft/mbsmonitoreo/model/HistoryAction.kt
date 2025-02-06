package com.dnegusoft.mbsmonitoreo.model

sealed interface HistoryAction {
    data object OnBack: HistoryAction
    data object OnSendPending: HistoryAction
}