package com.ktm.ksurvey.data.network.service

import com.ktm.ksurvey.data.network.data.LoginRequest
import com.ktm.ksurvey.data.network.data.LoginResponse
import com.ktm.ksurvey.data.network.data.LogoutRequest
import com.ktm.ksurvey.data.network.data.LogoutResponse
import com.ktm.ksurvey.data.network.data.RefreshTokenRequest
import com.ktm.ksurvey.data.network.data.RefreshTokenResponse
import com.ktm.ksurvey.data.network.data.ResetPasswordRequest
import com.ktm.ksurvey.data.network.data.ResetPasswordResponse
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

    @POST("api/v1/passwords")
    suspend fun resetPassword(
        @Body resetPasswordRequest: ResetPasswordRequest
    ): ResetPasswordResponse
}