package com.ktm.ksurvey.data.network.api

import com.ktm.ksurvey.data.network.data.BaseRequest
import com.ktm.ksurvey.data.network.data.FetchProfileResponse
import com.ktm.ksurvey.data.network.service.UserService
import com.ktm.ksurvey.data.storage.UserStore
import kotlinx.coroutines.delay
import javax.inject.Inject

class FetchProfileApi @Inject constructor(
    private val userService: UserService,
    private val userStore: UserStore
) : BaseApi<BaseRequest?, FetchProfileResponse>() {

    override suspend fun execute(request: BaseRequest?): FetchProfileResponse {
        return runCatching {
            delay(500L)
            val user = userStore.getUser()
            val authorization = "${user?.tokenType} ${user?.accessToken}"
            userService.fetchProfile(authorization)
        }.getOrElse {
            FetchProfileResponse(data = null).apply { errorCode = getHttpErrorCode(it) }
        }
    }
}