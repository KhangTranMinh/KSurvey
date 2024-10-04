package com.ktm.ksurvey.data.network.api

import com.ktm.ksurvey.data.network.ErrorCode
import com.ktm.ksurvey.data.network.data.BaseRequest
import com.ktm.ksurvey.data.network.data.FetchProfileResponse
import com.ktm.ksurvey.data.network.service.UserService
import com.ktm.ksurvey.data.storage.UserStore
import javax.inject.Inject

class FetchProfileApi @Inject constructor(
    private val userService: UserService,
    private val userStore: UserStore
) : BaseApi<BaseRequest?, FetchProfileResponse>() {

    override suspend fun execute(request: BaseRequest?): FetchProfileResponse {
        return runCatching {
            val user = userStore.getUser()
            val authorization = "${user?.tokenType} ${user?.accessToken}"
            userService.fetchProfile(authorization)
        }.getOrElse {
            // handle API error code here
            FetchProfileResponse(data = null).apply {
                this.errorCode = ErrorCode.UNKNOWN
            }
        }
    }
}