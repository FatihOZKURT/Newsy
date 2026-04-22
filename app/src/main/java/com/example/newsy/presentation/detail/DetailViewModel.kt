package com.example.newsy.presentation.detail

import androidx.lifecycle.ViewModel
import com.example.newsy.domain.model.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DetailUiState(
    val article: Article? = null,
    val isLoading: Boolean = false
)

class DetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadArticle(articleId: String) {
        // Mock data fetch
        _uiState.value = _uiState.value.copy(isLoading = true)
        val mockArticle = Article(articleId, "Haber Başlığı Burada Yer Alacak (ID: $articleId)", "Mock Source", null, "", "General", "10 min ago")
        _uiState.value = _uiState.value.copy(article = mockArticle, isLoading = false)
    }
}
