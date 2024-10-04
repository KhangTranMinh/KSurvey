package com.ktm.ksurvey.data.storage

import com.ktm.ksurvey.domain.entity.User

interface UserStore {

    suspend fun getUser(): User?

    suspend fun updateUser(user: User)
}