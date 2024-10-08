package com.ktm.ksurvey.domain.usecase

import com.ktm.ksurvey.domain.entity.Survey
import com.ktm.ksurvey.domain.entity.User
import com.ktm.ksurvey.domain.repository.SurveyRepository
import com.ktm.ksurvey.domain.repository.UserRepository
import com.ktm.ksurvey.domain.repository.result.Error
import com.ktm.ksurvey.domain.repository.result.Result

class SurveyUseCase(
    private val userRepository: UserRepository,
    private val surveyRepository: SurveyRepository
) {

    var isForceRefresh: Boolean = false

    suspend fun getUser(): User? {
        return userRepository.getUser()
    }

    suspend fun getSurveys(pageNumber: Int, pageSize: Int): Result<List<Survey>, Error> {
        return surveyRepository.getSurveys(isForceRefresh, pageNumber, pageSize)
    }

    suspend fun clearSurveyData() {
        surveyRepository.clearData()
    }
}