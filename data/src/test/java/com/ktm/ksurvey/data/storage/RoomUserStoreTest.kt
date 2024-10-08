package com.ktm.ksurvey.data.storage

import com.ktm.ksurvey.data.storage.room.RoomUserStore
import com.ktm.ksurvey.data.storage.room.db.UserDao
import com.ktm.ksurvey.data.storage.room.model.UserDbModel
import com.ktm.ksurvey.domain.entity.User
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RoomUserStoreTest {

    @MockK
    lateinit var userDao: UserDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getUsers_Null() {
        runTest {
            // mock
            coEvery { userDao.getUser() } returns null

            val result = RoomUserStore(userDao).getUser()
            assertEquals(null, result)
        }
    }

    @Test
    fun getUsers_NonNull() {
        runTest {
            // mock
            coEvery { userDao.getUser() } returns UserDbModel(id = "")

            val result = RoomUserStore(userDao).getUser()
            assertEquals("", result?.id)
        }
    }

    @Test
    fun updateUser() {
        runTest {
            // mock
            coEvery { userDao.insert(any()) } returns Unit

            RoomUserStore(userDao).updateUser(user = User())
            coVerify(exactly = 1) { userDao.insert(any(UserDbModel::class)) }
        }
    }

    @Test
    fun delete() {
        runTest {
            // mock
            coEvery { userDao.delete() } returns Unit

            RoomUserStore(userDao).delete()
            coVerify(exactly = 1) { userDao.delete() }
        }
    }
}