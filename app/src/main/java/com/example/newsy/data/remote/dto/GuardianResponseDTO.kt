package com.example.newsy.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GuardianResponseDTO(
    val response: GuardianDataDTO
)

@Serializable
data class GuardianDataDTO(
    val status: String,
    val total: Int? = null,
    val startIndex: Int? = null,
    val pageSize: Int? = null,
    val currentPage: Int? = null,
    val pages: Int? = null,
    val orderBy: String? = null,
    val results: List<ArticleDTO>? = null,
    val content: ArticleDTO? = null
)

@Serializable
data class ArticleDTO(
    val id: String,
    val type: String,
    val sectionId: String,
    val sectionName: String,
    val webPublicationDate: String,
    val webTitle: String,
    val webUrl: String,
    val apiUrl: String,
    val fields: FieldsDTO? = null
)

@Serializable
data class FieldsDTO(
    val thumbnail: String? = null,
    val trailText: String? = null,
    val body: String? = null
)
