package com.ktm.ksurvey.data.network.service

import com.ktm.ksurvey.data.network.data.LoginRequest
import com.ktm.ksurvey.data.network.data.LoginResponse
import com.ktm.ksurvey.data.network.data.LogoutRequest
import com.ktm.ksurvey.data.network.data.LogoutResponse
import com.ktm.ksurvey.data.network.data.RefreshTokenRequest
import com.ktm.ksurvey.data.network.data.RefreshTokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("api/v1/oauth/token")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("api/v1/oauth/token")
    suspend fun refreshToken(
        @Body refreshTokenRequest: RefreshTokenRequest
    ): RefreshTokenResponse

    @POST("api/v1/oauth/revoke")
    suspend fun logout(
        @Body logoutRequest: LogoutRequest
    ): LogoutResponse
}