package com.example.newsy.data.repository

import androidx.paging.*
import app.cash.sqldelight.paging3.QueryPagingSource
import com.example.newsy.data.local.NewsyDatabase
import com.example.newsy.data.local.mediator.NewsRemoteMediator
import com.example.newsy.data.mapper.toDomain
import com.example.newsy.data.remote.api.GuardianApiService
import com.example.newsy.data.remote.dto.SectionDTO
import com.example.newsy.domain.model.Article
import com.example.newsy.domain.model.Interest
import com.example.newsy.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class NewsRepositoryImpl(
    private val apiService: GuardianApiService,
    private val database: NewsyDatabase
) : NewsRepository {

    private val json = Json { ignoreUnknownKeys = true }
    
    @OptIn(ExperimentalPagingApi::class)
    override fun getNews(category: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            remoteMediator = NewsRemoteMediator(
                apiService = apiService,
                database = database,
                category = category
            ),
            pagingSourceFactory = {
                QueryPagingSource(
                    countQuery = database.newsyDatabaseQueries.countArticlesByCategory(category),
                    transacter = database.newsyDatabaseQueries,
                    context = Dispatchers.IO,
                    queryProvider = { limit, offset ->
                        database.newsyDatabaseQueries.getArticlesByCategory(
                            category = category,
                            limit = limit,
                            offset = offset
                        )
                    }
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                Article(
                    id = entity.id,
                    title = entity.title,
                    imageUrl = entity.imageUrl,
                    category = entity.category,
                    time = entity.time,
                    webUrl = entity.webUrl,
                    description = entity.description,
                    body = entity.body
                )
            }
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
                        name = dto.webTitle.uppercase(),
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
