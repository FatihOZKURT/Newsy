package com.example.newsy.domain.repository

import com.example.newsy.domain.model.Article

interface NewsRepository {
    suspend fun getNews(category: String, page: Int): List<Article>
}
