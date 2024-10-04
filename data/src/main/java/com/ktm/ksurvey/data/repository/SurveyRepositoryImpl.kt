package com.ktm.ksurvey.data.repository

import com.ktm.ksurvey.domain.entity.Survey
import com.ktm.ksurvey.domain.repository.SurveyRepository
import com.ktm.ksurvey.domain.repository.result.Error
import com.ktm.ksurvey.domain.repository.result.Result

class SurveyRepositoryImpl : SurveyRepository {

    override suspend fun fetchSurveys(page: Int, count: Int): Result<List<Survey>, Error> {
        TODO("Not yet implemented")
    }
}