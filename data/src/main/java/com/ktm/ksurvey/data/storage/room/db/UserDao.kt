package com.ktm.ksurvey.data.storage.room.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ktm.ksurvey.data.storage.room.model.UserDbModel

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userDbModel: UserDbModel)

    @Query("SELECT * FROM userdbmodel LIMIT 1")
    fun getUser(): UserDbModel?
}