package com.ktm.ksurvey.data.network.data

import com.ktm.ksurvey.data.network.ErrorCode

interface BaseRequest

interface BaseResponse {

    var errorCode: Int

    fun isSuccess() = ErrorCode.SUCCESS == errorCode
}