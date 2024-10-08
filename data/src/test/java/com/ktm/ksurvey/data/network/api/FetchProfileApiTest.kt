package com.ktm.ksurvey.data.network.api

import com.ktm.ksurvey.data.network.ErrorCode
import com.ktm.ksurvey.data.network.data.FetchProfileRequest
import com.ktm.ksurvey.data.network.data.FetchProfileResponse
import com.ktm.ksurvey.data.network.service.UserService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class FetchProfileApiTest {

    @MockK
    lateinit var userService: UserService

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun execute_Response_Exception() {
        runTest {
            // mock
            coEvery { userService.fetchProfile(any()) } throws Exception()

            val result = FetchProfileApi(userService).execute(
                FetchProfileRequest(tokenType = anyString(), accessToken = anyString())
            )
            assertEquals(ErrorCode.UNKNOWN, result.errorCode)
        }
    }

    @Test
    fun execute_Response_Success() {
        runTest {
            // mock
            coEvery {
                userService.fetchProfile(any())
            } returns FetchProfileResponse(data = null).apply {
                errorCode = ErrorCode.SUCCESS
            }

            val result = FetchProfileApi(userService).execute(
                FetchProfileRequest(tokenType = anyString(), accessToken = anyString())
            )
            assertEquals(ErrorCode.SUCCESS, result.errorCode)
        }
    }
}