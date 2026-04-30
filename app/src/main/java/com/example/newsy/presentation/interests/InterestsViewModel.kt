package com.example.newsy.presentation.interests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsy.data.local.UserPreferencesRepository
import com.example.newsy.domain.model.Interest
import com.example.newsy.domain.repository.NewsRepository
import com.example.newsy.util.Constants
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
                var sections = newsRepository.getSections()
                
                if (sections.isEmpty()) {
                    // İnternet yoksa veya hata oluştuysa Constants listesini kullan
                    sections = Constants.ALLOWED_SECTIONS.map { id ->
                        Interest(
                            id = id,
                            name = Constants.getCategoryDisplayName(id),
                            isSelected = false
                        )
                    }.sortedBy { it.name }
                }

                // İlk 2 kategoriyi varsayılan olarak seçili yap
                val finalSections = sections.mapIndexed { index, interest ->
                    if (index < 2) interest.copy(isSelected = true) else interest
                }

                _uiState.update { it.copy(interests = finalSections, isLoading = false) }
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
