package com.ktm.ksurvey.data.network.api

import com.ktm.ksurvey.data.network.ErrorCode
import com.ktm.ksurvey.data.network.data.BaseRequest
import com.ktm.ksurvey.data.network.data.BaseResponse
import retrofit2.HttpException

abstract class BaseApi<Request : BaseRequest?, Response : BaseResponse> {

    abstract suspend fun execute(request: Request): Response?

    protected fun getHttpErrorCode(throwable: Throwable): Int {
        return if (throwable is HttpException) {
            throwable.code()
        } else {
            ErrorCode.UNKNOWN
        }
    }
}