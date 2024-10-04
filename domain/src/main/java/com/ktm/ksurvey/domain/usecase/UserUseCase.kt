package com.ktm.ksurvey.domain.usecase

import com.ktm.ksurvey.domain.entity.User
import com.ktm.ksurvey.domain.repository.UserRepository
import com.ktm.ksurvey.domain.repository.result.Error
import com.ktm.ksurvey.domain.repository.result.Result

class UserUseCase(
    private val userRepository: UserRepository
) {

    suspend fun saveUser(user: User) {
        userRepository.saveUser(user)
    }

    suspend fun getUser(): Result<User, Error> {
        return userRepository.getUser()
    }
}