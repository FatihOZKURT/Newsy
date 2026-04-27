package com.example.newsy.data.repository

import com.example.newsy.data.mapper.toDomain
import com.example.newsy.data.remote.api.GuardianApiService
import com.example.newsy.data.remote.dto.ArticleDTO
import com.example.newsy.data.remote.dto.SectionDTO
import com.example.newsy.domain.model.Article
import com.example.newsy.domain.model.Interest
import com.example.newsy.domain.repository.NewsRepository
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class NewsRepositoryImpl(
    private val apiService: GuardianApiService
) : NewsRepository {

    private val json = Json { ignoreUnknownKeys = true }
    
    override suspend fun getNews(category: String, page: Int): List<Article> {
        return try {
            val response = apiService.getNews(category, page)
            response.response.results?.mapNotNull { element ->
                try {
                    val dto = json.decodeFromJsonElement<ArticleDTO>(element)
                    dto.toDomain()
                } catch (e: Exception) {
                    null
                }
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getArticleDetail(articleId: String): Article? {
        return try {
            val response = apiService.getArticleDetail(articleId)
            response.response.content?.toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getSections(): List<Interest> {
        return try {
            val response = apiService.getSections()
            response.response.results?.mapNotNull { element ->
                try {
                    val dto = json.decodeFromJsonElement<SectionDTO>(element)
                    Interest(
                        id = dto.id,
                        name = dto.webTitle.uppercase(), // Veri seviyesinde büyük harfe çeviriyoruz
                        isSelected = false
                    )
                } catch (e: Exception) {
                    null
                }
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
