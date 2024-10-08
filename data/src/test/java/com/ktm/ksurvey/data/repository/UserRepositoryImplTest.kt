package com.ktm.ksurvey.data.repository

import com.ktm.ksurvey.data.network.ErrorCode
import com.ktm.ksurvey.data.network.api.FetchProfileApi
import com.ktm.ksurvey.data.network.api.LoginApi
import com.ktm.ksurvey.data.network.api.LogoutApi
import com.ktm.ksurvey.data.network.api.RefreshTokenApi
import com.ktm.ksurvey.data.network.data.FetchProfileResponse
import com.ktm.ksurvey.data.network.data.LoginResponse
import com.ktm.ksurvey.data.network.data.LogoutResponse
import com.ktm.ksurvey.data.network.data.RefreshTokenResponse
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
import org.mockito.ArgumentMatchers.anyString

class UserRepositoryImplTest {

    @MockK
    lateinit var loginApi: LoginApi

    @MockK
    lateinit var refreshTokenApi: RefreshTokenApi

    @MockK
    lateinit var fetchProfileApi: FetchProfileApi

    @MockK
    lateinit var logoutApi: LogoutApi

    @MockK
    lateinit var userStore: UserStore

    @MockK
    lateinit var surveyStore: SurveyStore

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun login_Response_Error() {
        runTest {
            // mock
            coEvery {
                loginApi.execute(any())
            } returns LoginResponse(data = null).apply {
                errorCode = ErrorCode.UNKNOWN
            }

            val result = UserRepositoryImpl(
                loginApi, refreshTokenApi, fetchProfileApi, logoutApi, userStore, surveyStore
            ).login(email = anyString(), password = anyString())
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
    fun login_Response_Success() {
        runTest {
            // mock
            coEvery {
                loginApi.execute(any())
            } returns LoginResponse(data = null).apply {
                errorCode = ErrorCode.SUCCESS
            }
            coEvery {
                fetchProfileApi.execute(any())
            } returns FetchProfileResponse(data = null).apply {
                errorCode = ErrorCode.SUCCESS
            }
            coEvery { userStore.updateUser(any()) } just runs

            val result = UserRepositoryImpl(
                loginApi, refreshTokenApi, fetchProfileApi, logoutApi, userStore, surveyStore
            ).login(email = anyString(), password = anyString())
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
    fun logout_Response_Error() {
        runTest {
            // mock
            coEvery { userStore.getUser() } returns User()
            coEvery {
                logoutApi.execute(any())
            } returns LogoutResponse().apply {
                errorCode = ErrorCode.UNKNOWN
            }

            val result = UserRepositoryImpl(
                loginApi, refreshTokenApi, fetchProfileApi, logoutApi, userStore, surveyStore
            ).logout()
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
    fun logout_Response_Success() {
        runTest {
            // mock
            coEvery { userStore.getUser() } returns User()
            coEvery { userStore.delete() } just runs
            coEvery { surveyStore.delete() } just runs
            coEvery {
                logoutApi.execute(any())
            } returns LogoutResponse().apply {
                errorCode = ErrorCode.SUCCESS
            }

            val result = UserRepositoryImpl(
                loginApi, refreshTokenApi, fetchProfileApi, logoutApi, userStore, surveyStore
            ).logout()
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
    fun refreshToken_Response_Error() {
        runTest {
            // mock
            coEvery { userStore.getUser() } returns User()
            coEvery {
                refreshTokenApi.execute(any())
            } returns RefreshTokenResponse(data = null).apply {
                errorCode = ErrorCode.UNKNOWN
            }

            val result = UserRepositoryImpl(
                loginApi, refreshTokenApi, fetchProfileApi, logoutApi, userStore, surveyStore
            ).refreshToken()
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
    fun refreshToken_Response_Success() {
        runTest {
            // mock
            coEvery { userStore.getUser() } returns User()
            coEvery { userStore.updateUser(any()) } just runs
            coEvery {
                refreshTokenApi.execute(any())
            } returns RefreshTokenResponse(data = null).apply {
                errorCode = ErrorCode.SUCCESS
            }

            val result = UserRepositoryImpl(
                loginApi, refreshTokenApi, fetchProfileApi, logoutApi, userStore, surveyStore
            ).refreshToken()
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