package com.ktm.ksurvey.data.network.api

import com.ktm.ksurvey.data.network.ErrorCode
import com.ktm.ksurvey.data.network.data.FetchSurveysRequest
import com.ktm.ksurvey.data.network.data.FetchSurveysResponse
import com.ktm.ksurvey.data.network.service.SurveyService
import com.ktm.ksurvey.data.storage.UserStore
import com.ktm.ksurvey.domain.entity.User
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt

class FetchSurveysApiTest {

    @MockK
    lateinit var surveyService: SurveyService

    @MockK
    lateinit var userStore: UserStore

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun execute_Response_Exception() {
        runTest {
            // mock
            coEvery { userStore.getUser() } returns User()
            coEvery { surveyService.fetchSurveys(any(), any(), any()) } throws Exception()

            val result = FetchSurveysApi(surveyService, userStore).execute(
                FetchSurveysRequest(pageNumber = anyInt(), pageSize = anyInt())
            )
            assertEquals(ErrorCode.UNKNOWN, result.errorCode)
        }
    }

    @Test
    fun execute_Response_Success() {
        runTest {
            // mock
            coEvery { userStore.getUser() } returns User()
            coEvery {
                surveyService.fetchSurveys(any(), any(), any())
            } returns FetchSurveysResponse(data = null).apply {
                errorCode = ErrorCode.SUCCESS
            }

            val result = FetchSurveysApi(surveyService, userStore).execute(
                FetchSurveysRequest(pageNumber = anyInt(), pageSize = anyInt())
            )
            assertEquals(ErrorCode.SUCCESS, result.errorCode)
        }
    }
}