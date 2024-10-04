package com.ktm.ksurvey.data.storage.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ktm.ksurvey.data.storage.room.model.SurveyDbModel
import com.ktm.ksurvey.data.storage.room.model.UserDbModel

@Database(
    entities = [
        UserDbModel::class,
        SurveyDbModel::class
    ],
    version = 1
)
abstract class KSurveyDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun surveyDao(): SurveyDao
}