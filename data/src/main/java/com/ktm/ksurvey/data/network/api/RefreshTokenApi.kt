package com.ktm.ksurvey.data.network.api

import com.ktm.ksurvey.data.network.data.RefreshTokenRequest
import com.ktm.ksurvey.data.network.data.RefreshTokenResponse
import com.ktm.ksurvey.data.network.service.AuthService
import javax.inject.Inject

class RefreshTokenApi @Inject constructor(
    private val authService: AuthService,
) : BaseApi<RefreshTokenRequest, RefreshTokenResponse>() {

    override suspend fun execute(request: RefreshTokenRequest): RefreshTokenResponse {
        return runCatching {
            authService.refreshToken(request)
        }.getOrElse {
            RefreshTokenResponse(data = null).apply { errorCode = getHttpErrorCode(it) }
        }
    }
}