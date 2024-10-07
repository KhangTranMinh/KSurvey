package com.ktm.ksurvey.data.network.api

import com.ktm.ksurvey.data.network.data.FetchProfileRequest
import com.ktm.ksurvey.data.network.data.FetchProfileResponse
import com.ktm.ksurvey.data.network.service.UserService
import javax.inject.Inject

class FetchProfileApi @Inject constructor(
    private val userService: UserService,
) : BaseApi<FetchProfileRequest, FetchProfileResponse>() {

    override suspend fun execute(request: FetchProfileRequest): FetchProfileResponse {
        return runCatching {
            val authorization = "${request.tokenType} ${request.accessToken}"
            userService.fetchProfile(authorization)
        }.getOrElse {
            FetchProfileResponse(data = null).apply { errorCode = getHttpErrorCode(it) }
        }
    }
}