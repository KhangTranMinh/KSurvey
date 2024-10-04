package com.ktm.ksurvey.domain.repository.result

sealed class Result<out D, out E> {

    /**
     * Repository return it when it completes successfully with a specific data
     */
    data class Success<out D>(val data: D) : Result<D, Nothing>()

    /**
     * Repository return it when it fails with an specific error
     */
    data class Error<out E>(val error: E) : Result<Nothing, E>()
}