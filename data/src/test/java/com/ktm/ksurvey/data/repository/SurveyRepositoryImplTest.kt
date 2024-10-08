package com.ktm.ksurvey.data.repository

import com.ktm.ksurvey.data.network.ErrorCode
import com.ktm.ksurvey.data.network.api.FetchSurveysApi
import com.ktm.ksurvey.data.network.data.FetchSurveysResponse
import com.ktm.ksurvey.data.storage.SurveyStore
import com.ktm.ksurvey.data.storage.UserStore
import com.ktm.ksurvey.domain.entity.User
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyInt

class SurveyRepositoryImplTest {

    @MockK
    lateinit var fetchSurveysApi: FetchSurveysApi

    @MockK
    lateinit var userStore: UserStore

    @MockK
    lateinit var surveyStore: SurveyStore

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getSurveys_User_Null() {
        runTest {
            // mock
            coEvery { userStore.getUser() } returns null

            val result = SurveyRepositoryImpl(
                fetchSurveysApi, userStore, surveyStore
            ).getSurveys(isForceRefresh = anyBoolean(), pageNumber = anyInt(), pageSize = anyInt())
            when (result) {
                is com.ktm.ksurvey.domain.repository.result.Result.Error -> {
                    assertTrue(true)
                }

                is com.ktm.ksurvey.domain.repository.result.Result.Success -> {
                    fail()
                }
            }
        }
    }

    @Test
    fun getSurveys_ForceRefresh() {
        runTest {
            // mock
            coEvery { userStore.getUser() } returns User()
            coEvery { surveyStore.delete() } just runs
            coEvery { surveyStore.saveSurveys(any()) } just runs
            coEvery {
                surveyStore.getSurveys(any(), any(), any())
            } returns arrayListOf()
            coEvery {
                fetchSurveysApi.execute(any())
            } returns FetchSurveysResponse(data = null).apply {
                errorCode = ErrorCode.SUCCESS
            }

            val result = SurveyRepositoryImpl(
                fetchSurveysApi, userStore, surveyStore
            ).getSurveys(isForceRefresh = true, pageNumber = 0, pageSize = anyInt())
            when (result) {
                is com.ktm.ksurvey.domain.repository.result.Result.Error -> {
                    fail()
                }

                is com.ktm.ksurvey.domain.repository.result.Result.Success -> {
                    assertTrue(true)
                }
            }
        }
    }

    @Test
    fun getSurveys() {
        runTest {
            // mock
            coEvery { userStore.getUser() } returns User()
            coEvery { surveyStore.delete() } just runs
            coEvery { surveyStore.saveSurveys(any()) } just runs
            coEvery {
                surveyStore.getSurveys(any(), any(), any())
            } returns arrayListOf()
            coEvery {
                fetchSurveysApi.execute(any())
            } returns FetchSurveysResponse(data = null).apply {
                errorCode = ErrorCode.SUCCESS
            }

            val result = SurveyRepositoryImpl(
                fetchSurveysApi, userStore, surveyStore
            ).getSurveys(isForceRefresh = false, pageNumber = anyInt(), pageSize = anyInt())
            when (result) {
                is com.ktm.ksurvey.domain.repository.result.Result.Error -> {
                    fail()
                }

                is com.ktm.ksurvey.domain.repository.result.Result.Success -> {
                    assertTrue(true)
                }
            }
        }
    }
}