package com.ktm.ksurvey.domain.repository

import com.ktm.ksurvey.domain.entity.Survey
import com.ktm.ksurvey.domain.repository.result.Error
import com.ktm.ksurvey.domain.repository.result.Result

interface SurveyRepository {

    suspend fun fetchSurveys(page: Int, count: Int): Result<List<Survey>, Error>
}