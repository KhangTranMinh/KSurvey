package com.ktm.ksurvey.data.storage.room

import com.ktm.ksurvey.data.storage.UserStore
import com.ktm.ksurvey.data.storage.room.db.UserDao
import com.ktm.ksurvey.data.storage.room.model.UserDbModel
import com.ktm.ksurvey.domain.entity.User
import javax.inject.Inject

class RoomUserStore @Inject constructor(
    private val userDao: UserDao
) : UserStore {

    override suspend fun getUser(): User? {
        return userDao.getUser()?.toUser()
    }

    override suspend fun updateUser(user: User) {
        userDao.insert(UserDbModel.fromUser(user))
    }
}