package com.ktm.ksurvey.data.network.api

import com.ktm.ksurvey.data.network.data.LoginRequest
import com.ktm.ksurvey.data.network.data.LoginResponse
import com.ktm.ksurvey.data.network.service.AuthService
import kotlinx.coroutines.delay
import javax.inject.Inject

class LoginApi @Inject constructor(
    private val authService: AuthService
) : BaseApi<LoginRequest, LoginResponse>() {

    override suspend fun execute(request: LoginRequest): LoginResponse {
        return runCatching {
            delay(500L)
            authService.login(request)
        }.getOrElse {
            LoginResponse(data = null).apply { errorCode = getHttpErrorCode(it) }
        }
    }
}