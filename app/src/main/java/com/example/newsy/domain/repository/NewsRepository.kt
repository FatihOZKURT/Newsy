package com.example.newsy.domain.repository

import androidx.paging.PagingData
import com.example.newsy.domain.model.Article
import com.example.newsy.domain.model.Interest
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(category: String): Flow<PagingData<Article>>
    suspend fun getArticleDetail(articleId: String): Article?
    suspend fun getSections(): List<Interest>
}
