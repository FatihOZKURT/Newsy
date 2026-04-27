package com.example.newsy.data.remote.api

import com.example.newsy.BuildConfig
import com.example.newsy.data.remote.dto.GuardianResponseDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class GuardianApiService(private val client: HttpClient) {
    
    companion object {
        private const val BASE_URL = "https://content.guardianapis.com"
        private val API_KEY = BuildConfig.GUARDIAN_API_KEY
    }

    suspend fun getNews(category: String, page: Int = 1): GuardianResponseDTO {
        return client.get("$BASE_URL/search") {
            parameter("section", category)
            parameter("page", page)
            parameter("show-fields", "thumbnail")
            parameter("api-key", API_KEY)
        }.body()
    }

    suspend fun getArticleDetail(articleId: String): GuardianResponseDTO {
        return client.get("$BASE_URL/$articleId") {
            parameter("show-fields", "thumbnail,trailText,body")
            parameter("api-key", API_KEY)
        }.body()
    }

    suspend fun getSections(): GuardianResponseDTO {
        return client.get("$BASE_URL/sections") {
            parameter("api-key", API_KEY)
        }.body()
    }
}
