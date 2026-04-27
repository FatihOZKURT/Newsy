package com.example.newsy.presentation.interests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsy.data.local.UserPreferencesRepository
import com.example.newsy.domain.model.Interest
import com.example.newsy.domain.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class InterestsUiState(
    val interests: List<Interest> = emptyList(),
    val isLoading: Boolean = false
)

class InterestsViewModel(
    private val newsRepository: NewsRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InterestsUiState())
    val uiState: StateFlow<InterestsUiState> = _uiState.asStateFlow()

    init {
        loadInterests()
    }

    private fun loadInterests() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Sadece ilk 40 kategoriyi alıyoruz
                val sections = newsRepository.getSections().take(40).mapIndexed { index, interest ->
                    if (index < 2) interest.copy(isSelected = true) else interest
                }
                if (sections.isEmpty()) {
                    val fallback = listOf(
                        Interest("news", "News"),
                        Interest("technology", "Technology"),
                        Interest("business", "Business"),
                        Interest("science", "Science"),
                        Interest("sport", "Sport")
                    )
                    _uiState.update { it.copy(interests = fallback, isLoading = false) }
                } else {
                    _uiState.update { it.copy(interests = sections, isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    suspend fun completeOnboarding() {
        val selected = _uiState.value.interests
            .filter { it.isSelected }
            .map { it.id }
            .toSet()
        
        userPreferencesRepository.saveSelectedCategories(selected)
        userPreferencesRepository.setInterestsSelected(true)
    }

    fun toggleInterest(index: Int) {
        _uiState.update { currentState ->
            val updatedList = currentState.interests.toMutableList()
            val item = updatedList[index]
            updatedList[index] = item.copy(isSelected = !item.isSelected)
            currentState.copy(interests = updatedList)
        }
    }
}
