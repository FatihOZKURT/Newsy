package com.example.newsy.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsy.domain.model.Article
import com.example.newsy.domain.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val categories: List<String> = listOf("Highlights", "All", "News", "Crypto", "Tech", "Science"),
    val selectedCategory: String = "Highlights",
    val articles: List<Article> = emptyList(),
    val selectedTab: Int = 0,
    val isLoading: Boolean = false
)

class HomeViewModel(
    private val repository: NewsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadArticles(_uiState.value.selectedCategory)
    }

    private fun loadArticles(category: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = repository.getNews(category, 1)
            _uiState.value = _uiState.value.copy(
                articles = result,
                isLoading = false
            )
        }
    }

    fun selectCategory(category: String) {
        if (_uiState.value.selectedCategory == category) return
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        loadArticles(category)
    }

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
    }
}
