package com.example.newsy.presentation.interests

import androidx.lifecycle.ViewModel
import com.example.newsy.domain.model.Interest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class InterestsUiState(
    val interests: List<Interest> = emptyList()
)

class InterestsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(InterestsUiState())
    val uiState: StateFlow<InterestsUiState> = _uiState.asStateFlow()

    init {
        loadInterests()
    }

    private fun loadInterests() {
        val initialInterests = listOf(
            Interest("NEWS", true),
            Interest("TECHNOLOGY", true),
            Interest("BUSINESS & FINANCE"),
            Interest("SCIENCE"),
            Interest("HISTORY"),
            Interest("ARTIFICIAL INTELLIGENCE"),
            Interest("INVESTMENT"),
            Interest("FASHION"),
            Interest("NASA"),
            Interest("CRYPTO CURRENCY"),
            Interest("FOOTBALL"),
            Interest("ENTREPRENEURSHIP"),
            Interest("SPORTS"),
            Interest("SUSTAINABILITY"),
            Interest("WARS AND CONFLICTS"),
            Interest("TENNIS"),
            Interest("BASKETBALL"),
            Interest("BOOKS"),
            Interest("MOVIES"),
            Interest("MUSIC"),
            Interest("ART"),
            Interest("HEALTH"),
            Interest("TRAVEL"),
            Interest("FOOD"),
            Interest("GAMING"),
            Interest("POLITICS"),
            Interest("EDUCATION"),
            Interest("PSYCHOLOGY"),
            Interest("PHILOSOPHY"),
            Interest("ENVIRONMENT"),
            Interest("SPACE"),
            Interest("AUTOMOTIVE"),
            Interest("ARCHITECTURE"),
            Interest("LIFESTYLE"),
            Interest("FITNESS"),
            Interest("E-SPORTS"),
            Interest("VR/AR"),
            Interest("ROBOTICS")
        )
        _uiState.update { it.copy(interests = initialInterests) }
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
