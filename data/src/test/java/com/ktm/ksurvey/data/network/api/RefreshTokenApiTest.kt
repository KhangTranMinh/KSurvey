package com.ktm.ksurvey.data.network.api

import com.ktm.ksurvey.data.network.ErrorCode
import com.ktm.ksurvey.data.network.data.RefreshTokenRequest
import com.ktm.ksurvey.data.network.data.RefreshTokenResponse
import com.ktm.ksurvey.data.network.service.AuthService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class RefreshTokenApiTest {

    @MockK
    lateinit var authService: AuthService

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun execute_Response_Exception() {
        runTest {
            // mock
            coEvery { authService.refreshToken(any()) } throws Exception()

            val result = RefreshTokenApi(authService).execute(
                RefreshTokenRequest(refreshToken = anyString())
            )
            assertEquals(ErrorCode.UNKNOWN, result.errorCode)
        }
    }

    @Test
    fun execute_Response_Success() {
        runTest {
            // mock
            coEvery {
                authService.refreshToken(any())
            } returns RefreshTokenResponse(data = null).apply {
                errorCode = ErrorCode.SUCCESS
            }

            val result = RefreshTokenApi(authService).execute(
                RefreshTokenRequest(refreshToken = anyString())
            )
            assertEquals(ErrorCode.SUCCESS, result.errorCode)
        }
    }
}