package com.example.newsy.presentation.home

import androidx.lifecycle.ViewModel
import com.example.newsy.domain.model.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeUiState(
    val categories: List<String> = listOf("Highlights", "All", "News", "Crypto Currency", "Tech", "Science"),
    val selectedCategory: String = "Highlights",
    val articles: List<Article> = emptyList(),
    val selectedTab: Int = 0
)

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadArticles()
    }

    private fun loadArticles() {
        val initialArticles = listOf(
            Article("1", "UK passes anti-tobacco bill to create a 'smoke-free' generation", "EURONEWS", null, "", "World", "1 hour"),
            Article("2", "Duolingo is now giving free users access to advanced learning content", "TECHCRUNCH", null, "", "Tech", "1 hour"),
            Article("3", "The most interesting startups showcased at Google Cloud Next 2026", "TECHCRUNCH", null, "", "Tech", "2 hours"),
            Article("4", "AI in Healthcare: What to expect in the next decade", "REUTERS", null, "", "Science", "3 hours"),
            Article("5", "SpaceX prepares for next Starship launch attempt", "NASA", null, "", "Space", "4 hours"),
            Article("6", "New findings in deep sea exploration reveal unknown species", "NATIONAL GEOGRAPHIC", null, "", "Nature", "5 hours")
        )
        _uiState.value = _uiState.value.copy(articles = initialArticles)
    }

    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
    }
}
