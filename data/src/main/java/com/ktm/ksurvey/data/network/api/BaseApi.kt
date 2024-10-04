package com.ktm.ksurvey.data.network.api

import com.ktm.ksurvey.data.network.data.BaseRequest
import com.ktm.ksurvey.data.network.data.BaseResponse

abstract class BaseApi<Request : BaseRequest?, Response : BaseResponse> {

    abstract suspend fun execute(request: Request): Response?
}