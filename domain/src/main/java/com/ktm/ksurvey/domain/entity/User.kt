package com.ktm.ksurvey.domain.entity

import java.util.UUID

class User private constructor(
    var id: String = "",
    var email: String = "",
    var name: String = "",
    var avatarUrl: String = "",
    var accessToken: String = "",
    var tokenType: String = "",
    var expiresIn: Long = 0L,
    var refreshToken: String = "",
    var createdAt: Long = 0L,
) {

    val tokenExpiryTimeMillis: Long
        get() = (createdAt + expiresIn) * 1000L

    fun isAccessTokenValid(): Boolean {
        return accessToken.isNotBlank() && System.currentTimeMillis() < tokenExpiryTimeMillis
    }

    fun shouldRefreshToken(): Boolean {
        val diff = tokenExpiryTimeMillis - System.currentTimeMillis()
        return accessToken.isNotBlank() && diff < TOKEN_EXPIRY_THRESHOLD
    }

    companion object {
        const val TOKEN_EXPIRY_THRESHOLD = 600_000L // 10 minutes

        fun createUser(): User = User(id = UUID.randomUUID().toString())
    }
}