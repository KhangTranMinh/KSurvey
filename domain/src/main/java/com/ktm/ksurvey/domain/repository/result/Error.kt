package com.ktm.ksurvey.domain.repository.result

sealed class Error {

    data class GeneralError(val throwable: Throwable) : Error()

    data class ApiError(val errorCode: Int) : Error()
}