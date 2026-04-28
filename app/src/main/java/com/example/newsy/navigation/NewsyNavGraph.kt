package com.example.newsy.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.newsy.presentation.detail.DetailScreen
import com.example.newsy.presentation.interests.InterestsScreen
import com.example.newsy.presentation.main.MainScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Loading : Route
    @Serializable
    data object Interests : Route
    @Serializable
    data object Main : Route
    @Serializable
    data class Detail(val articleId: String) : Route
}

@Composable
fun NewsyNavGraph(
    viewModel: NavigationViewModel = koinViewModel()
) {
    if (viewModel.backStack.isEmpty()) return

    NavDisplay(
        backStack = viewModel.backStack,
        onBack = { viewModel.pop() },
        entryProvider = { key ->
            when (key) {
                is Route.Interests -> NavEntry(key) {
                    InterestsScreen(onStartClick = {
                        viewModel.navigateToHome()
                    })
                }
                is Route.Main -> NavEntry(key) {
                    MainScreen(onArticleClick = { id ->
                        viewModel.push(Route.Detail(id))
                    })
                }
                is Route.Detail -> NavEntry(key) {
                    DetailScreen(
                        articleId = key.articleId,
                        onBackClick = { viewModel.pop() }
                    )
                }
                else -> NavEntry(Unit) { 
                    // Handle unknown
                }
            }
        }
    )
}
