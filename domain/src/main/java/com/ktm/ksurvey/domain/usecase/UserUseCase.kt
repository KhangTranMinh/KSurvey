package com.ktm.ksurvey.domain.usecase

import com.ktm.ksurvey.domain.entity.User
import com.ktm.ksurvey.domain.repository.UserRepository
import com.ktm.ksurvey.domain.repository.result.Error
import com.ktm.ksurvey.domain.repository.result.Result

class UserUseCase(
    private val userRepository: UserRepository
) {

    suspend fun login(email: String, password: String): Result<User, Error> {
        val loginResult = userRepository.login(email, password)
        return if (loginResult is Result.Success) {
            val fetchProfileResult = userRepository.fetchProfile()
            if (fetchProfileResult is Result.Success) {
                Result.Success(data = fetchProfileResult.data)
            } else {
                fetchProfileResult
            }
        } else {
            loginResult
        }
    }

    suspend fun validateUserToken(): Boolean {
        val user = userRepository.getUser()

        if (user == null || !user.isAccessTokenValid()) return false

        if (!user.shouldRefreshToken()) return true

        val refreshTokenResult = userRepository.refreshToken()
        return refreshTokenResult is Result.Success
    }

    suspend fun logout() {
    }
}