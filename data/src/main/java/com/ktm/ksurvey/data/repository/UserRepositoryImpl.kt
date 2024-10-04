package com.ktm.ksurvey.data.repository

import com.ktm.ksurvey.domain.entity.User
import com.ktm.ksurvey.domain.repository.UserRepository
import com.ktm.ksurvey.domain.repository.result.Error
import com.ktm.ksurvey.domain.repository.result.Result

class UserRepositoryImpl : UserRepository {

    override suspend fun saveUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(): Result<User, Error> {
        TODO("Not yet implemented")
    }
}