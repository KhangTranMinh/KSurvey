package com.ktm.ksurvey.domain.repository

import com.ktm.ksurvey.domain.entity.Survey
import com.ktm.ksurvey.domain.repository.result.Error
import com.ktm.ksurvey.domain.repository.result.Result

interface SurveyRepository {

    suspend fun getSurveys(
        isForceRefresh: Boolean,
        pageNumber: Int,
        pageSize: Int
    ): Result<List<Survey>, Error>

    suspend fun clearData()
}