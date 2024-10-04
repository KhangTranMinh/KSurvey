package com.ktm.ksurvey.data.network.api

import com.ktm.ksurvey.data.network.ErrorCode
import com.ktm.ksurvey.data.network.data.LoginRequest
import com.ktm.ksurvey.data.network.data.LoginResponse
import com.ktm.ksurvey.data.network.service.AuthService
import javax.inject.Inject

class LoginApi @Inject constructor(
    private val authService: AuthService
) : BaseApi<LoginRequest, LoginResponse>() {

    override suspend fun execute(request: LoginRequest): LoginResponse {
        return runCatching {
            authService.login(request)
        }.getOrElse {
            LoginResponse(data = null).apply {
                errorCode = ErrorCode.UNKNOWN
            }
        }
    }
}