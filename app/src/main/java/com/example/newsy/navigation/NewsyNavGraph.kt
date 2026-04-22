package com.example.newsy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.newsy.presentation.home.HomeScreen
import com.example.newsy.presentation.interests.InterestsScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Interests : Route
    @Serializable
    data object Home : Route
}

@Composable
fun NewsyNavGraph() {
    val backStack = remember { mutableStateListOf<Any>(Route.Interests) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is Route.Interests -> NavEntry(key) {
                    InterestsScreen(onStartClick = {
                        backStack.add(Route.Home)
                    })
                }
                is Route.Home -> NavEntry(key) {
                    HomeScreen()
                }
                else -> NavEntry(Unit) { 
                    // Handle unknown
                }
            }
        }
    )
}
