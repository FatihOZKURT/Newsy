package com.example.newsy.data.remote.api

import com.example.newsy.data.remote.dto.GuardianResponseDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class GuardianApiService(private val client: HttpClient) {
    
    companion object {
        private const val BASE_URL = "https://content.guardianapis.com"
        private const val API_KEY = "test" // Geliştirme aşaması için varsayılan key
    }

    suspend fun getNews(category: String, page: Int = 1): GuardianResponseDTO {
        return client.get("$BASE_URL/search") {
            parameter("q", category)
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
}
