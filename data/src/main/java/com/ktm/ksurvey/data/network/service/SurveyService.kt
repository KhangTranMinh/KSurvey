package com.ktm.ksurvey.data.network.service

import retrofit2.http.GET
import retrofit2.http.Query

interface SurveyService {

    @GET("api/v1/surveys?page[number]={number}&page[size]={size}")
    suspend fun fetchUserDetails(
        @Query("number") number: Int,
        @Query("size") size: Int,
    )
}