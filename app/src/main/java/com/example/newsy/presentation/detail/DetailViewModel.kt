package com.example.newsy.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsy.domain.model.Article
import com.example.newsy.domain.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetailUiState(
    val article: Article? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class DetailViewModel(
    private val repository: NewsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadArticle(articleId: String) {
        if (_uiState.value.article?.id == articleId) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = repository.getArticleDetail(articleId)
            if (result != null) {
                _uiState.value = _uiState.value.copy(article = result, isLoading = false)
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to load article details")
            }
        }
    }
}
