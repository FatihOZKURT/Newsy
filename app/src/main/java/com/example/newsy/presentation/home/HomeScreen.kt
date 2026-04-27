package com.example.newsy.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.newsy.domain.model.Article
import kotlinx.coroutines.launch

@Composable
fun HomeFeedContent(
    uiState: HomeUiState,
    onCategorySelect: (String) -> Unit,
    onArticleClick: (String) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Newsy Menu", modifier = Modifier.padding(16.dp), fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Profile") },
                    selected = false,
                    onClick = { /* Handle */ },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("About") },
                    selected = false,
                    onClick = { /* Handle */ },
                    icon = { Icon(Icons.Default.Info, contentDescription = null) }
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            // Top Bar
            Surface(
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        scope.launch { drawerState.open() }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.Black)
                    }

                    SecondaryScrollableTabRow(
                        selectedTabIndex = uiState.categories.indexOf(uiState.selectedCategory),
                        edgePadding = 8.dp,
                        containerColor = Color.Transparent,
                        contentColor = Color.Black,
                        divider = {},
                        indicator = {},
                        modifier = Modifier.weight(1f)
                    ) {
                        uiState.categories.forEach { category ->
                            Tab(
                                selected = uiState.selectedCategory == category,
                                onClick = { onCategorySelect(category) },
                                text = {
                                    Text(
                                        text = category.uppercase(),
                                        fontSize = 14.sp,
                                        fontWeight = if (uiState.selectedCategory == category) FontWeight.Bold else FontWeight.Normal,
                                        color = if (uiState.selectedCategory == category) Color.Black else Color.Gray
                                    )
                                }
                            )
                        }
                    }

                    Icon(Icons.Default.Tune, contentDescription = "Filter", tint = Color.Black)
                }
            }

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Black)
                }
            } else {
                // News Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.articles) { article ->
                        NewsGridItem(article = article) {
                            onArticleClick(article.id)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewsGridItem(
    article: Article,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = article.imageUrl,
                contentDescription = article.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = article.category.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = article.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp,
                        color = Color.Black
                    )
                }

                Text(
                    text = article.time.substringBefore("T").ifEmpty { "Today" },
                    fontSize = 10.sp,
                    color = Color.LightGray
                )
            }
        }
    }
}
