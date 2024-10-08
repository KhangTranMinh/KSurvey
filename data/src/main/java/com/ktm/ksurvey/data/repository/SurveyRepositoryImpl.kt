package com.ktm.ksurvey.data.repository

import com.ktm.ksurvey.data.network.api.FetchSurveysApi
import com.ktm.ksurvey.data.network.data.FetchSurveysRequest
import com.ktm.ksurvey.data.storage.SurveyStore
import com.ktm.ksurvey.data.storage.UserStore
import com.ktm.ksurvey.data.util.log
import com.ktm.ksurvey.domain.entity.Survey
import com.ktm.ksurvey.domain.repository.SurveyRepository
import com.ktm.ksurvey.domain.repository.result.Error
import com.ktm.ksurvey.domain.repository.result.Result
import com.ktm.ksurvey.domain.repository.result.exception.NoUserFoundException
import javax.inject.Inject

class SurveyRepositoryImpl @Inject constructor(
    private val fetchSurveysApi: FetchSurveysApi,
    private val userStore: UserStore,
    private val surveyStore: SurveyStore
) : SurveyRepository {

    override suspend fun getSurveys(
        isForceRefresh: Boolean,
        pageNumber: Int,
        pageSize: Int
    ): Result<List<Survey>, Error> {
        val user = userStore.getUser()
        return if (user == null) {
            Result.Error(error = Error.GeneralError(NoUserFoundException()))
        } else {
            // force refresh with paging is complicated
            // so I come with a simple solution:
            //   only if isForceRefresh = true and pageNumber is 0
            //     1. fetch page 0 from remote
            //     2. remove all records in local storage
            //     3. update new page 0 into local storage
            //   then back to the normal behavior, check local storage first, if not found, fetch remote
            if (isForceRefresh && pageNumber == 0) {
                log("force refresh")
                val forceRefreshResult = fetchSurveysRemote(
                    userId = user.id, pageNumber = 1, pageSize = pageSize
                )
                if (forceRefreshResult is Result.Success) {
                    log("delete all records and add the new page 0")
                    surveyStore.delete()
                    surveyStore.saveSurveys(forceRefreshResult.data)
                }
            }

            // try to get from local storage first
            val surveys = surveyStore.getSurveys(
                userId = user.id, pageNumber = pageNumber, pageSize = pageSize
            )
            if (surveys.isEmpty()) {
                // if no record, fetch from remote and save data
                val result = fetchSurveysRemote(
                    userId = user.id, pageNumber = pageNumber + 1, pageSize = pageSize
                )
                if (result is Result.Success) {
                    surveyStore.saveSurveys(result.data)
                }
                result
            } else {
                Result.Success(surveys)
            }
        }
    }

    private suspend fun fetchSurveysRemote(
        userId: String,
        pageNumber: Int,
        pageSize: Int
    ): Result<List<Survey>, Error> {
        val response = fetchSurveysApi.execute(
            FetchSurveysRequest(pageNumber = pageNumber, pageSize = pageSize)
        )
        return if (response.isSuccess()) {
            val surveys = ArrayList<Survey>()
            response.data?.forEach {
                val attributes = it.attributes
                surveys.add(
                    Survey(
                        id = it.id ?: "",
                        title = attributes?.title ?: "",
                        description = attributes?.description ?: "",
                        isActive = attributes?.isActive ?: false,
                        coverImageUrl = attributes?.coverImageUrl ?: "",
                        userId = userId,
                    )
                )
            }
            log("fetch remote | pageNumber: $pageNumber, pageSize: $pageSize, surveys: ${surveys.size}")
            Result.Success(surveys)
        } else {
            Result.Error(error = Error.ApiError(errorCode = response.errorCode))
        }
    }

    override suspend fun clearData() {
        surveyStore.delete()
    }
}