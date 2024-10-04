package com.ktm.ksurvey.data.storage.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class UserDbModel(
    @PrimaryKey val id: String,
)