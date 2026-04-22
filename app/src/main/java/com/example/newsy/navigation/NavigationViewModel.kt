package com.example.newsy.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsy.data.local.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NavigationViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    var backStack by mutableStateOf<List<Any>>(listOf(Route.Loading))
        private set

    init {
        viewModelScope.launch {
            val isSelected = userPreferencesRepository.isInterestsSelected.first()
            if (isSelected) {
                backStack = listOf(Route.Home)
            } else {
                backStack = listOf(Route.Interests)
            }
        }
    }

    fun push(route: Any) {
        backStack = backStack + route
    }

    fun pop() {
        if (backStack.size > 1) {
            backStack = backStack.dropLast(1)
        }
    }

    fun navigateToHome() {
        if (backStack.size == 1 && backStack.first() == Route.Home) return
        backStack = listOf(Route.Home)
    }
}
