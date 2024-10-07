package com.ktm.ksurvey.data.network.api

import com.ktm.ksurvey.data.network.data.FetchSurveysRequest
import com.ktm.ksurvey.data.network.data.FetchSurveysResponse
import com.ktm.ksurvey.data.network.service.SurveyService
import com.ktm.ksurvey.data.storage.UserStore
import javax.inject.Inject

class FetchSurveysApi @Inject constructor(
    private val surveyService: SurveyService,
    private val userStore: UserStore
) : BaseApi<FetchSurveysRequest, FetchSurveysResponse>() {

    override suspend fun execute(request: FetchSurveysRequest): FetchSurveysResponse {
        return runCatching {
            val user = userStore.getUser()
            val authorization = "${user?.tokenType} ${user?.accessToken}"
            surveyService.fetchSurveys(
                authorization = authorization,
                pageNumber = request.pageNumber,
                pageSize = request.pageSize
            )
        }.getOrElse {
            FetchSurveysResponse(data = null).apply { errorCode = getHttpErrorCode(it) }
        }
    }
}