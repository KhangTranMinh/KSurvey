package com.ktm.ksurvey.data.network.data

import com.google.gson.annotations.SerializedName
import com.ktm.ksurvey.data.network.ApiConfig
import com.ktm.ksurvey.data.network.ErrorCode

class ResetPasswordRequest(
    @SerializedName("user") val user: User,
    @SerializedName("client_id") val clientId: String = ApiConfig.CLIENT_ID,
    @SerializedName("client_secret") val clientSecret: String = ApiConfig.CLIENT_SECRET,
) : BaseRequest {

    class User(
        @SerializedName("email") val email: String,
    )
}

class ResetPasswordResponse(
    @SerializedName("meta") val meta: Meta?,
) : BaseResponse {

    override var errorCode: Int = ErrorCode.SUCCESS
}