package com.ktm.ksurvey.data.network.data

import com.google.gson.annotations.SerializedName
import com.ktm.ksurvey.data.network.ApiConfig
import com.ktm.ksurvey.data.network.ErrorCode

class LogoutRequest(
    @SerializedName("token") val token: String,
    @SerializedName("client_id") val clientId: String = ApiConfig.CLIENT_ID,
    @SerializedName("client_secret") val clientSecret: String = ApiConfig.CLIENT_SECRET,
) : BaseRequest

class LogoutResponse : BaseResponse {

    override var errorCode: Int = ErrorCode.SUCCESS
}