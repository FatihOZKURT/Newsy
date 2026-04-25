package com.example.newsy.presentation.main

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.newsy.presentation.explore.ExploreScreen
import com.example.newsy.presentation.home.HomeFeedContent
import com.example.newsy.presentation.home.HomeViewModel
import com.example.newsy.presentation.settings.SettingsScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    homeViewModel: HomeViewModel = koinViewModel(),
    onArticleClick: (String) -> Unit
) {
    val homeUiState by homeViewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Fiziksel geri tuşunu yakala ve ana sayfadayken Interest ekranına dönüşü engelle
    BackHandler(enabled = homeUiState.selectedTab == 0) {
        (context as? Activity)?.finish()
    }

    val navItems = listOf(
        "Home" to Icons.Default.Home,
        "Explore" to Icons.Default.Search,
        "Settings" to Icons.Default.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = homeUiState.selectedTab == index,
                        onClick = { homeViewModel.selectTab(index) },
                        icon = { Icon(item.second, contentDescription = item.first) },
                        label = { Text(item.first) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            unselectedIconColor = Color.Gray,
                            indicatorColor = Color.LightGray.copy(alpha = 0.3f)
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (homeUiState.selectedTab) {
                0 -> HomeFeedContent(
                    uiState = homeUiState,
                    onCategorySelect = homeViewModel::selectCategory,
                    onArticleClick = onArticleClick
                )
                1 -> ExploreScreen()
                2 -> SettingsScreen()
            }
        }
    }
}
