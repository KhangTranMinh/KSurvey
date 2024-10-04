package com.ktm.ksurvey.data.network.service

import com.ktm.ksurvey.data.network.data.FetchProfileResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface UserService {

    @GET("api/v1/me")
    suspend fun fetchProfile(
        @Header("Authorization") authorization: String
    ): FetchProfileResponse
}