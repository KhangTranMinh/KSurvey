package com.ktm.ksurvey.domain.repository

import com.ktm.ksurvey.domain.entity.User
import com.ktm.ksurvey.domain.repository.result.Error
import com.ktm.ksurvey.domain.repository.result.Result

interface UserRepository {

    suspend fun login(email: String, password: String): Result<User, Error>

    suspend fun refreshToken(): Result<User, Error>

    suspend fun logout(): Result<Any, Error>

    suspend fun resetPassword(email: String): Result<String, Error>

    suspend fun getUser(): User?

    suspend fun clearData()
}