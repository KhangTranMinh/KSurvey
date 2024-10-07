package com.ktm.ksurvey.data.network.data

import com.google.gson.annotations.SerializedName
import com.ktm.ksurvey.data.network.ErrorCode

class FetchProfileResponse(
    @SerializedName("data") val data: Data?,
) : BaseResponse {

    override var errorCode: Int = ErrorCode.SUCCESS

    class Data(
        @SerializedName("id") val id: String?,
        @SerializedName("type") val type: String?,
        @SerializedName("attributes") val attributes: Attributes?,
    ) {

        class Attributes(
            @SerializedName("email") val email: String?,
            @SerializedName("name") val name: String?,
            @SerializedName("avatar_url") val avatarUrl: String?,
        )
    }
}