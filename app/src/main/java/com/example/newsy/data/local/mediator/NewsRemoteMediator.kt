package com.example.newsy.data.local.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.newsy.data.local.ArticleEntity
import com.example.newsy.data.local.NewsyDatabase
import com.example.newsy.data.remote.api.GuardianApiService
import com.example.newsy.data.remote.dto.ArticleDTO
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val apiService: GuardianApiService,
    private val database: NewsyDatabase,
    private val category: String
) : RemoteMediator<Int, ArticleEntity>() {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = database.newsyDatabaseQueries.getRemoteKeyById(
                        id = state.lastItemOrNull()?.id ?: return MediatorResult.Success(endOfPaginationReached = false)
                    ).executeAsOneOrNull()
                    
                    if (remoteKey?.nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    remoteKey.nextKey.toInt()
                }
            }

            val response = apiService.getNews(category = category, page = page)
            val articles = response.response.results?.mapNotNull { element ->
                try {
                    json.decodeFromJsonElement<ArticleDTO>(element)
                } catch (e: Exception) {
                    null
                }
            } ?: emptyList()

            val endOfPaginationReached = articles.isEmpty()

            database.newsyDatabaseQueries.transaction {
                if (loadType == LoadType.REFRESH) {
                    database.newsyDatabaseQueries.clearRemoteKeysByCategory(category)
                    database.newsyDatabaseQueries.clearArticlesByCategory(category)
                }

                val prevKey = if (page == 1) null else (page - 1).toLong()
                val nextKey = if (endOfPaginationReached) null else (page + 1).toLong()

                articles.forEach { dto ->
                    database.newsyDatabaseQueries.insertArticle(
                        id = dto.id,
                        title = dto.webTitle,
                        imageUrl = dto.fields?.thumbnail,
                        category = category,
                        time = dto.webPublicationDate ?: "",
                        webUrl = dto.webUrl,
                        description = dto.fields?.trailText,
                        body = dto.fields?.body,
                        fetchedAt = System.currentTimeMillis()
                    )
                    database.newsyDatabaseQueries.insertKey(
                        id = dto.id,
                        prevKey = prevKey,
                        nextKey = nextKey,
                        category = category
                    )
                }
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
