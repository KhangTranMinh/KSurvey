package com.ktm.ksurvey.data.network.data

import com.google.gson.annotations.SerializedName
import com.ktm.ksurvey.data.network.ApiConfig
import com.ktm.ksurvey.data.network.ErrorCode

class RefreshTokenRequest(
    @SerializedName("grant_type") val grantType: String = ApiConfig.GRANT_TYPE_REFRESH_TOKEN,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("client_id") val clientId: String = ApiConfig.CLIENT_ID,
    @SerializedName("client_id") val clientSecret: String = ApiConfig.CLIENT_SECRET,
) : BaseRequest

class RefreshTokenResponse(
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