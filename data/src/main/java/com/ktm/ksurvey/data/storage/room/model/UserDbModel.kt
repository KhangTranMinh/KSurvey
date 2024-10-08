package com.ktm.ksurvey.data.storage.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ktm.ksurvey.data.security.CryptoUtil
import com.ktm.ksurvey.domain.entity.User

@Entity
class UserDbModel(
    @PrimaryKey val id: String,
    val email: String = "",
    val name: String = "",
    val avatarUrl: String = "",
    val accessToken: String = "",
    val accessTokenIv: String = "",
    val tokenType: String = "",
    val expiresIn: Long = 0L,
    val refreshToken: String = "",
    val refreshTokenIv: String = "",
    val createdAt: Long = 0L,
) {

    fun toUser(): User {
        return User().also {
            it.id = this.id
            it.name = this.name
            it.email = this.email
            it.avatarUrl = this.avatarUrl
            it.accessToken = CryptoUtil.decrypt(Pair(this.accessToken, this.accessTokenIv))
            it.tokenType = this.tokenType
            it.expiresIn = this.expiresIn
            it.refreshToken = CryptoUtil.decrypt(Pair(this.refreshToken, this.refreshTokenIv))
            it.createdAt = this.createdAt
        }
    }

    companion object {
        fun fromUser(user: User): UserDbModel {
            val accessTokenPair = CryptoUtil.encrypt(user.accessToken)
            val refreshTokenPair = CryptoUtil.encrypt(user.refreshToken)
            return UserDbModel(
                id = user.id,
                name = user.name,
                email = user.email,
                avatarUrl = user.avatarUrl,
                accessToken = accessTokenPair.first,
                accessTokenIv = accessTokenPair.second,
                tokenType = user.tokenType,
                expiresIn = user.expiresIn,
                refreshToken = refreshTokenPair.first,
                refreshTokenIv = refreshTokenPair.second,
                createdAt = user.createdAt,
            )
        }
    }
}