package com.ktm.ksurvey.data.repository

import com.ktm.ksurvey.data.network.api.FetchProfileApi
import com.ktm.ksurvey.data.network.api.LoginApi
import com.ktm.ksurvey.data.network.api.LogoutApi
import com.ktm.ksurvey.data.network.api.RefreshTokenApi
import com.ktm.ksurvey.data.network.data.FetchProfileRequest
import com.ktm.ksurvey.data.network.data.LoginRequest
import com.ktm.ksurvey.data.network.data.LogoutRequest
import com.ktm.ksurvey.data.network.data.RefreshTokenRequest
import com.ktm.ksurvey.data.storage.SurveyStore
import com.ktm.ksurvey.data.storage.UserStore
import com.ktm.ksurvey.domain.entity.User
import com.ktm.ksurvey.domain.repository.UserRepository
import com.ktm.ksurvey.domain.repository.result.Error
import com.ktm.ksurvey.domain.repository.result.Result
import com.ktm.ksurvey.domain.repository.result.exception.NoUserFoundException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val loginApi: LoginApi,
    private val refreshTokenApi: RefreshTokenApi,
    private val fetchProfileApi: FetchProfileApi,
    private val logoutApi: LogoutApi,
    private val userStore: UserStore,
    private val surveyStore: SurveyStore,
) : UserRepository {

    override suspend fun login(email: String, password: String): Result<User, Error> {
        // do login first
        val response = loginApi.execute(
            LoginRequest(email = email, password = password)
        )
        return if (response.isSuccess()) {
            val attributes = response.data?.attributes
            val tokenData = TokenData(
                accessToken = attributes?.accessToken ?: "",
                tokenType = attributes?.tokenType ?: "",
                expiresIn = attributes?.expiresIn ?: 0L,
                refreshToken = attributes?.refreshToken ?: "",
                createdAt = attributes?.createdAt ?: 0L
            )
            // then fetch profile
            return fetchProfile(tokenData)
        } else {
            Result.Error(error = Error.ApiError(errorCode = response.errorCode))
        }
    }

    override suspend fun refreshToken(): Result<User, Error> {
        val user = userStore.getUser()
        return if (user == null) {
            Result.Error(error = Error.GeneralError(NoUserFoundException()))
        } else {
            val response = refreshTokenApi.execute(
                RefreshTokenRequest(refreshToken = user.refreshToken)
            )
            if (response.isSuccess()) {
                val attributes = response.data?.attributes
                user.apply {
                    accessToken = attributes?.accessToken ?: ""
                    tokenType = attributes?.tokenType ?: ""
                    expiresIn = attributes?.expiresIn ?: 0L
                    refreshToken = attributes?.refreshToken ?: ""
                    createdAt = attributes?.createdAt ?: 0L
                }
                userStore.updateUser(user)
                Result.Success(data = user)
            } else {
                Result.Error(error = Error.ApiError(errorCode = response.errorCode))
            }
        }
    }

    override suspend fun logout(): Result<Any, Error> {
        val user = userStore.getUser()
        return if (user == null) {
            Result.Error(error = Error.GeneralError(NoUserFoundException()))
        } else {
            val response = logoutApi.execute(
                LogoutRequest(token = user.accessToken)
            )
            if (response.isSuccess()) {
                userStore.delete()
                surveyStore.delete()
                Result.Success(Any())
            } else {
                Result.Error(error = Error.ApiError(errorCode = response.errorCode))
            }
        }
    }

    private suspend fun fetchProfile(tokenData: TokenData): Result<User, Error> {
        val response = fetchProfileApi.execute(
            FetchProfileRequest(tokenData.tokenType, tokenData.accessToken)
        )
        return if (response.isSuccess()) {
            val user = User().apply {
                id = response.data?.id ?: ""

                val attributes = response.data?.attributes
                email = attributes?.email ?: ""
                name = attributes?.name ?: ""
                avatarUrl = attributes?.avatarUrl ?: ""

                accessToken = tokenData.accessToken
                tokenType = tokenData.tokenType
                expiresIn = tokenData.expiresIn
                refreshToken = tokenData.refreshToken
                createdAt = tokenData.createdAt
            }
            userStore.updateUser(user)
            Result.Success(data = user)
        } else {
            Result.Error(error = Error.ApiError(errorCode = response.errorCode))
        }
    }

    override suspend fun getUser(): User? {
        return userStore.getUser()
    }

    override suspend fun clearData() {
        userStore.delete()
    }

    private class TokenData(
        val accessToken: String,
        val tokenType: String,
        val expiresIn: Long,
        val refreshToken: String,
        val createdAt: Long,
    )
}