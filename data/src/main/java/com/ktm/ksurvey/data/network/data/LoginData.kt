package com.ktm.ksurvey.data.network.data

import com.google.gson.annotations.SerializedName
import com.ktm.ksurvey.data.network.ApiConfig
import com.ktm.ksurvey.data.network.ErrorCode

class LoginRequest(
    @SerializedName("grant_type") val grantType: String = ApiConfig.GRANT_TYPE_PASSWORD,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("client_id") val clientId: String = ApiConfig.CLIENT_ID,
    @SerializedName("client_secret") val clientSecret: String = ApiConfig.CLIENT_SECRET,
) : BaseRequest

class LoginResponse(
    @SerializedName("data") val data: Data?,
) : BaseResponse {

    override var errorCode: Int = ErrorCode.SUCCESS

    class Data(
        @SerializedName("id") val id: String?,
        @SerializedName("type") val type: String?,
        @SerializedName("attributes") val attributes: Attributes?,
    ) {

        class Attributes(
            @SerializedName("access_token") val accessToken: String?,
            @SerializedName("token_type") val tokenType: String?,
            @SerializedName("expires_in") val expiresIn: Long?,
            @SerializedName("refresh_token") val refreshToken: String?,
            @SerializedName("created_at") val createdAt: Long?,
        )
    }
}