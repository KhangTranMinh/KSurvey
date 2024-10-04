package com.ktm.ksurvey.data.repository

import com.ktm.ksurvey.data.network.api.FetchProfileApi
import com.ktm.ksurvey.data.network.api.LoginApi
import com.ktm.ksurvey.data.network.api.RefreshTokenApi
import com.ktm.ksurvey.data.network.data.LoginRequest
import com.ktm.ksurvey.data.storage.UserStore
import com.ktm.ksurvey.domain.entity.User
import com.ktm.ksurvey.domain.repository.UserRepository
import com.ktm.ksurvey.domain.repository.result.Error
import com.ktm.ksurvey.domain.repository.result.Result
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val loginApi: LoginApi,
    private val refreshTokenApi: RefreshTokenApi,
    private val fetchProfileApi: FetchProfileApi,
    private val userStore: UserStore
) : UserRepository {

    override suspend fun login(email: String, password: String): Result<User, Error> {
        val response = loginApi.execute(
            LoginRequest(email = email, password = password)
        )
        return if (response.isSuccess()) {
            val attributes = response.data?.attributes
            val user = User.createUser().apply {
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

    override suspend fun refreshToken(): Result<User, Error> {
        val user = userStore.getUser()
        return if (user == null) {
            Result.Error(error = Error.GeneralError(NullPointerException()))
        } else {
            val response = refreshTokenApi.execute(
                request = null
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

    override suspend fun fetchProfile(): Result<User, Error> {
        val user = userStore.getUser()
        return if (user == null) {
            Result.Error(error = Error.GeneralError(NullPointerException()))
        } else {
            val response = fetchProfileApi.execute(
                request = null
            )
            if (response.isSuccess()) {
                val attributes = response.data?.attributes
                user.apply {
                    email = attributes?.name ?: ""
                    name = attributes?.name ?: ""
                    avatarUrl = attributes?.name ?: ""
                }
                userStore.updateUser(user)
                Result.Success(data = user)
            } else {
                Result.Error(error = Error.ApiError(errorCode = response.errorCode))
            }
        }
    }

    override suspend fun updateUser(user: User) {
        userStore.updateUser(user)
    }

    override suspend fun getUser(): User? {
        return userStore.getUser()
    }
}