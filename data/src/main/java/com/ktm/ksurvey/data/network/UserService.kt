package com.ktm.ksurvey.data.network

import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {

    @POST("api/v1/oauth/token")
    suspend fun login()

    @POST("api/v1/oauth/token")
    suspend fun refreshToken()

    @GET("api/v1/me")
    suspend fun getProfile()
}