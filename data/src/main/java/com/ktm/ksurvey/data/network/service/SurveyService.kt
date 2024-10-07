package com.ktm.ksurvey.data.network.service

import com.ktm.ksurvey.data.network.data.FetchSurveysResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SurveyService {

    @GET("api/v1/surveys")
    suspend fun fetchSurveys(
        @Header("Authorization") authorization: String,
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") pageSize: Int,
    ): FetchSurveysResponse
}