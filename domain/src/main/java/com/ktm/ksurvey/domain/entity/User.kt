package com.ktm.ksurvey.domain.entity

class User(
    val id: String = "",
    val email: String = "",
    val type: String = "",
    val accessToken: String = "",
    val tokenType: String = "",
    val expiresIn: Long = 0L,
    val refreshToken: String = "",
    val createdAt: Long = 0L,
)