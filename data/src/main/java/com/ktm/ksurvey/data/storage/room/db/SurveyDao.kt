package com.ktm.ksurvey.data.storage.room.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ktm.ksurvey.data.storage.room.model.SurveyDbModel

@Dao
interface SurveyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg surveyDbModels: SurveyDbModel)

    @Query("SELECT * FROM surveydbmodel WHERE userId = :userId LIMIT :limit OFFSET :offset")
    fun getSurveys(userId: String, limit: Int, offset: Int): List<SurveyDbModel>

    @Query("DELETE FROM surveydbmodel")
    suspend fun delete()
}