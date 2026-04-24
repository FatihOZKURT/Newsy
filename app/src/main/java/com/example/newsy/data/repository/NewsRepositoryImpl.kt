package com.example.newsy.data.repository

import com.example.newsy.data.mapper.toDomain
import com.example.newsy.data.remote.api.GuardianApiService
import com.example.newsy.domain.model.Article
import com.example.newsy.domain.repository.NewsRepository

class NewsRepositoryImpl(
    private val apiService: GuardianApiService
) : NewsRepository {
    
    override suspend fun getNews(category: String, page: Int): List<Article> {
        return try {
            val response = apiService.getNews(category, page)
            response.response.results?.map { it.toDomain() } ?: emptyList()
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
}
