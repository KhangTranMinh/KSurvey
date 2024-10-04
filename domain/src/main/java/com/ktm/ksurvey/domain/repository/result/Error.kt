package com.ktm.ksurvey.domain.repository.result

sealed class Error {

    /**
     * Repository returns this error if there is any exception
     * @param throwable General error with a throwable
     */
    data class GeneralError(val throwable: Throwable) : Error()

    /**
     * Repository return this error if an API fails
     * @param errorCode Error from API
     */
    data class ApiError(val errorCode: Int) : Error()
}