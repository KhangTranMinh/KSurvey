package com.ktm.ksurvey.data.network.api

import com.ktm.ksurvey.data.network.ErrorCode
import com.ktm.ksurvey.data.network.data.LoginRequest
import com.ktm.ksurvey.data.network.data.LoginResponse
import com.ktm.ksurvey.data.network.service.AuthService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class LoginApiTest {

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
            coEvery { authService.login(any()) } throws Exception()

            val result = LoginApi(authService).execute(
                LoginRequest(email = anyString(), password = anyString())
            )
            assertEquals(ErrorCode.UNKNOWN, result.errorCode)
        }
    }

    @Test
    fun execute_Response_Success() {
        runTest {
            // mock
            coEvery {
                authService.login(any())
            } returns LoginResponse(data = null).apply {
                errorCode = ErrorCode.SUCCESS
            }

            val result = LoginApi(authService).execute(
                LoginRequest(email = anyString(), password = anyString())
            )
            assertEquals(ErrorCode.SUCCESS, result.errorCode)
        }
    }
}