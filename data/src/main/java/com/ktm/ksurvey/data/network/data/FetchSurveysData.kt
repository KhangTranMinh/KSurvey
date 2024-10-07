package com.ktm.ksurvey.data.network.data

import com.google.gson.annotations.SerializedName
import com.ktm.ksurvey.data.network.ErrorCode

class FetchSurveysRequest(
    val pageNumber: Int,
    val pageSize: Int
) : BaseRequest

class FetchSurveysResponse(
    @SerializedName("data") val data: List<Data>?,
) : BaseResponse {

    override var errorCode: Int = ErrorCode.SUCCESS

    class Data(
        @SerializedName("id") val id: String?,
        @SerializedName("type") val type: String?,
        @SerializedName("attributes") val attributes: Attributes?,
    ) {

        class Attributes(
            @SerializedName("title") val title: String?,
            @SerializedName("description") val description: String?,
            @SerializedName("is_active") val isActive: Boolean,
            @SerializedName("cover_image_url") val coverImageUrl: String?,
        )
    }
}