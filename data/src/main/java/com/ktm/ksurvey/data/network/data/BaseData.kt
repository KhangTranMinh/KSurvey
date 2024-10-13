package com.ktm.ksurvey.data.network.data

import com.google.gson.annotations.SerializedName
import com.ktm.ksurvey.data.network.ErrorCode

interface BaseRequest

interface BaseResponse {

    var errorCode: Int

    fun isSuccess() = ErrorCode.SUCCESS == errorCode
}

class ErrorItem(
    @SerializedName("source") val source: String,
    @SerializedName("detail") val detail: String,
    @SerializedName("code") val code: String,
)

class Meta(
    @SerializedName("message") val message: String,
)