package com.ktm.ksurvey.domain.usecase

import com.ktm.ksurvey.domain.entity.User
import com.ktm.ksurvey.domain.repository.UserRepository
import com.ktm.ksurvey.domain.repository.result.Error
import com.ktm.ksurvey.domain.repository.result.Result

class UserUseCase(
    private val userRepository: UserRepository
) {

    suspend fun login(email: String, password: String): Result<User, Error> {
        return userRepository.login(email, password)
    }

    suspend fun validateUserToken(): Boolean {
        val user = userRepository.getUser()

        if (user == null || !user.isAccessTokenValid()) return false

        if (!user.shouldRefreshToken()) return true

        val refreshTokenResult = userRepository.refreshToken()
        return refreshTokenResult is Result.Success
    }

    suspend fun clearUserData() {
        userRepository.clearData()
    }

    suspend fun logout(): Result<Any, Error> {
        return userRepository.logout()
    }
}