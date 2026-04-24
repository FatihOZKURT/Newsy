package com.example.newsy.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GuardianResponseDTO(
    val response: GuardianDataDTO
)

@Serializable
data class GuardianDataDTO(
    val status: String,
    val total: Int,
    val startIndex: Int,
    val pageSize: Int,
    val currentPage: Int,
    val pages: Int,
    val orderBy: String,
    val results: List<ArticleDTO>
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
    val thumbnail: String? = null
)
