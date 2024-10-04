package com.ktm.ksurvey.data.storage

import com.ktm.ksurvey.domain.entity.Survey

interface SurveyStore {

    suspend fun getSurveys(page: Int, count: Int): List<Survey>

    suspend fun saveSurveys(surveys: List<Survey>)
}