package com.ktm.ksurvey.data.network.api

import com.ktm.ksurvey.data.network.data.LogoutRequest
import com.ktm.ksurvey.data.network.data.LogoutResponse
import com.ktm.ksurvey.data.network.service.AuthService
import javax.inject.Inject

class LogoutApi @Inject constructor(
    private val authService: AuthService
) : BaseApi<LogoutRequest, LogoutResponse>() {

    override suspend fun execute(request: LogoutRequest): LogoutResponse {
        return runCatching {
            authService.logout(request)
        }.getOrElse {
            LogoutResponse().apply { errorCode = getHttpErrorCode(it) }
        }
    }
}