package com.ktm.ksurvey.data.storage.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ktm.ksurvey.domain.entity.Survey

@Entity
class SurveyDbModel(
    @PrimaryKey val id: String,
    val title: String = "",
    val description: String = "",
    val isActive: Boolean = false,
    val coverImageUrl: String = "",
    val userId: String = "",
) {

    fun toSurvey(): Survey {
        return Survey(
            id = id,
            title = title,
            description = description,
            isActive = isActive,
            coverImageUrl = coverImageUrl,
            userId = userId,
        )
    }

    companion object {
        fun fromSurvey(survey: Survey): SurveyDbModel {
            return SurveyDbModel(
                id = survey.id,
                title = survey.title,
                description = survey.description,
                isActive = survey.isActive,
                coverImageUrl = survey.coverImageUrl,
                userId = survey.userId,
            )
        }
    }
}