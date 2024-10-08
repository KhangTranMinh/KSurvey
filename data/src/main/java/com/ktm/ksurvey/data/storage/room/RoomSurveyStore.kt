package com.ktm.ksurvey.data.storage.room

import com.ktm.ksurvey.data.storage.SurveyStore
import com.ktm.ksurvey.data.storage.room.db.SurveyDao
import com.ktm.ksurvey.data.storage.room.model.SurveyDbModel
import com.ktm.ksurvey.domain.entity.Survey
import javax.inject.Inject

class RoomSurveyStore @Inject constructor(
    private val surveyDao: SurveyDao
) : SurveyStore {

    override suspend fun getSurveys(userId: String, pageNumber: Int, pageSize: Int): List<Survey> {
        val surveys = arrayListOf<Survey>()
        surveyDao.getSurveys(
            userId = userId, limit = pageSize, offset = pageNumber * pageSize
        ).forEach {
            surveys.add(it.toSurvey())
        }
        return surveys
    }

    override suspend fun saveSurveys(surveys: List<Survey>) {
        val surveyDbModels = ArrayList<SurveyDbModel>()
        surveys.forEach {
            surveyDbModels.add(SurveyDbModel.fromSurvey(it))
        }
        surveyDao.insertAll(*surveyDbModels.toTypedArray())
    }

    override suspend fun delete() {
        surveyDao.delete()
    }
}