package com.example.newsy.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val category: String,
    val time: String,
    val webUrl: String
)
