package com.example.newsy.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsy.data.mapper.toDomain
import com.example.newsy.data.remote.api.GuardianApiService
import com.example.newsy.data.remote.dto.ArticleDTO
import com.example.newsy.domain.model.Article
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class NewsPagingSource(
    private val apiService: GuardianApiService,
    private val category: String
) : PagingSource<Int, Article>() {

    private val json = Json { ignoreUnknownKeys = true }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: 1
        return try {
            val response = apiService.getNews(category = category, page = position)
            val articles = response.response.results?.mapNotNull { element ->
                try {
                    val dto = json.decodeFromJsonElement<ArticleDTO>(element)
                    dto.toDomain()
                } catch (e: Exception) {
                    null
                }
            } ?: emptyList()

            LoadResult.Page(
                data = articles,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (articles.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
