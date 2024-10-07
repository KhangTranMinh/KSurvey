package com.ktm.ksurvey.data.storage.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ktm.ksurvey.domain.entity.User

@Entity
class UserDbModel(
    @PrimaryKey val id: String,
    val email: String = "",
    val name: String = "",
    val avatarUrl: String = "",
    val accessToken: String = "",
    val tokenType: String = "",
    val expiresIn: Long = 0L,
    val refreshToken: String = "",
    val createdAt: Long = 0L,
) {

    fun toUser(): User {
        return User().also {
            it.id = this.id
            it.name = this.name
            it.email = this.email
            it.avatarUrl = this.avatarUrl
            it.accessToken = this.accessToken
            it.tokenType = this.tokenType
            it.expiresIn = this.expiresIn
            it.refreshToken = this.refreshToken
            it.createdAt = this.createdAt
        }
    }

    companion object {
        fun fromUser(user: User): UserDbModel {
            return UserDbModel(
                id = user.id,
                name = user.name,
                email = user.email,
                avatarUrl = user.avatarUrl,
                accessToken = user.accessToken,
                tokenType = user.tokenType,
                expiresIn = user.expiresIn,
                refreshToken = user.refreshToken,
                createdAt = user.createdAt,
            )
        }
    }
}