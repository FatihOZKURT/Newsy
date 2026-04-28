package com.example.newsy.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsy.data.local.UserPreferencesRepository
import com.example.newsy.domain.model.Article
import com.example.newsy.domain.repository.NewsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val categories: List<String> = emptyList(),
    val selectedCategory: String = "",
    val selectedTab: Int = 0
)

class HomeViewModel(
    private val repository: NewsRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _selectedCategory = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val articlesFlow: Flow<PagingData<Article>> = _selectedCategory
        .filter { it.isNotEmpty() }
        .flatMapLatest { category ->
            repository.getNews(category)
        }
        .cachedIn(viewModelScope)

    init {
        initializeCategories()
    }

    private fun initializeCategories() {
        viewModelScope.launch {
            val selected = userPreferencesRepository.selectedCategories.first().toList()
            if (selected.isNotEmpty()) {
                val initialCategory = selected.first()
                _uiState.value = _uiState.value.copy(
                    categories = selected,
                    selectedCategory = initialCategory
                )
                _selectedCategory.value = initialCategory
            }
        }
    }

    fun selectCategory(category: String) {
        if (_uiState.value.selectedCategory == category) return
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        _selectedCategory.value = category
    }

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
    }
}
