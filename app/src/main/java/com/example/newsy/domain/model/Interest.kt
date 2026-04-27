package com.example.newsy.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Interest(
    val id: String,
    val name: String,
    val isSelected: Boolean = false
)
