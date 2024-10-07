package com.ktm.ksurvey.data.network.api

import com.ktm.ksurvey.data.network.data.BaseRequest
import com.ktm.ksurvey.data.network.data.RefreshTokenRequest
import com.ktm.ksurvey.data.network.data.RefreshTokenResponse
import com.ktm.ksurvey.data.network.service.AuthService
import com.ktm.ksurvey.data.storage.UserStore
import kotlinx.coroutines.delay
import javax.inject.Inject

class RefreshTokenApi @Inject constructor(
    private val authService: AuthService,
    private val userStore: UserStore
) : BaseApi<BaseRequest?, RefreshTokenResponse>() {

    override suspend fun execute(request: BaseRequest?): RefreshTokenResponse {
        return runCatching {
            delay(500L)
            val user = userStore.getUser()
            authService.refreshToken(
                RefreshTokenRequest(refreshToken = user?.refreshToken ?: "")
            )
        }.getOrElse {
            RefreshTokenResponse(data = null).apply { errorCode = getHttpErrorCode(it) }
        }
    }
}