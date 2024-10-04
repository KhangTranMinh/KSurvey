package com.ktm.ksurvey.domain.repository

import com.ktm.ksurvey.domain.entity.User
import com.ktm.ksurvey.domain.repository.result.Error
import com.ktm.ksurvey.domain.repository.result.Result

interface UserRepository {

    suspend fun saveUser(user: User)

    suspend fun getUser(): Result<User, Error>
}