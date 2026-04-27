package com.example.newsy.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class GuardianResponseDTO(
    val response: GuardianDataDTO
)

@Serializable
data class GuardianDataDTO(
    val status: String,
    val userTier: String? = null,
    val total: Int? = null,
    val results: List<JsonElement>? = null, // Polymorphic parsing için JsonElement kullanıyoruz
    val content: ArticleDTO? = null
)

@Serializable
data class SectionDTO(
    val id: String,
    val webTitle: String,
    val webUrl: String,
    val apiUrl: String,
    val editions: List<EditionDTO>? = null
)

@Serializable
data class EditionDTO(
    val id: String,
    val webTitle: String,
    val webUrl: String,
    val apiUrl: String,
    val code: String
)

@Serializable
data class ArticleDTO(
    val id: String,
    val webTitle: String,
    val webUrl: String,
    val apiUrl: String,
    val type: String? = null,
    val sectionId: String? = null,
    val sectionName: String? = null,
    val webPublicationDate: String? = null,
    val fields: FieldsDTO? = null
)

@Serializable
data class FieldsDTO(
    val thumbnail: String? = null,
    val trailText: String? = null,
    val body: String? = null
)
