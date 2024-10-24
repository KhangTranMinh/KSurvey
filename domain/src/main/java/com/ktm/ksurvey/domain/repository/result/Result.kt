package com.ktm.ksurvey.domain.repository.result

sealed class Result<out D, out E> {

    data class Success<out D>(val data: D) : Result<D, Nothing>()

    data class Error<out E>(val error: E) : Result<Nothing, E>()
}