package com.ktm.ksurvey.data.storage

import com.ktm.ksurvey.domain.entity.Survey

interface SurveyStore {

    suspend fun getSurveys(userId: String, pageNumber: Int, pageSize: Int): List<Survey>

    suspend fun saveSurveys(surveys: List<Survey>)

    suspend fun delete()
}