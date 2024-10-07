package com.ktm.ksurvey.domain.entity

class Survey(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val isActive: Boolean = false,
    val coverImageUrl: String = "",
    val userId: String = "",
)