package com.example.newsy.domain.repository

import com.example.newsy.domain.model.Article
import com.example.newsy.domain.model.Interest

interface NewsRepository {
    suspend fun getNews(category: String, page: Int): List<Article>
    suspend fun getArticleDetail(articleId: String): Article?
    suspend fun getSections(): List<Interest>
}
