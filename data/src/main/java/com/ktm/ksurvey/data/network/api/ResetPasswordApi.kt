package com.ktm.ksurvey.data.network.api

import com.ktm.ksurvey.data.network.data.ResetPasswordRequest
import com.ktm.ksurvey.data.network.data.ResetPasswordResponse
import com.ktm.ksurvey.data.network.service.AuthService
import javax.inject.Inject

class ResetPasswordApi @Inject constructor(
    private val authService: AuthService
) : BaseApi<ResetPasswordRequest, ResetPasswordResponse>() {

    override suspend fun execute(request: ResetPasswordRequest): ResetPasswordResponse {
        return runCatching {
            authService.resetPassword(request)
        }.getOrElse {
            ResetPasswordResponse(meta = null).apply { errorCode = getHttpErrorCode(it) }
        }
    }
}