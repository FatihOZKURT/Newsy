package com.example.newsy.presentation.home

import android.app.Activity
import androidx.compose.foundation.Image
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.newsy.R
import com.example.newsy.domain.model.Article
import com.example.newsy.presentation.components.NewsImage
import com.example.newsy.util.Constants
import kotlinx.coroutines.launch

@Composable
fun HomeFeedContent(
    uiState: HomeUiState,
    articles: LazyPagingItems<Article>,
    onCategorySelect: (String) -> Unit,
    onArticleClick: (String) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // Manuel yenileme durumunu takip etmek için
    var isManualRefreshing by remember { mutableStateOf(false) }
    val isPagingRefreshing = articles.loadState.refresh is LoadState.Loading

    // Yenileme işlemi bittiğinde ve kısa bir süre geçtiğinde ikonu kapat
    LaunchedEffect(isPagingRefreshing) {
        if (!isPagingRefreshing) {
            kotlinx.coroutines.delay(500)
            isManualRefreshing = false
        }
    }

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
                                        text = Constants.getCategoryDisplayName(category),
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

            // News Content or Loading
            Box(modifier = Modifier.fillMaxSize()) {
                PullToRefreshBox(
                    isRefreshing = isManualRefreshing,
                    onRefresh = { 
                        isManualRefreshing = true
                        articles.refresh() 
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (isPagingRefreshing && !isManualRefreshing && articles.itemCount == 0) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Black
                        )
                    } else if (articles.itemCount == 0 && !isPagingRefreshing) {
                        EmptyState()
                    } else {
                        // News Grid
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(articles.itemCount) { index ->
                                val article = articles[index]
                                if (article != null) {
                                    NewsGridItem(article = article) {
                                        onArticleClick(article.id)
                                    }
                                }
                            }

                            // Alt kısımdaki hata durumları (append)
                            articles.apply {
                                if (loadState.append is LoadState.Error) {
                                    val e = loadState.append as LoadState.Error
                                    item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                                        Text(
                                            text = e.error.localizedMessage ?: "Error",
                                            color = Color.Red,
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.newsy_app_icon),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(20.dp))
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "This channel has no content to display",
            fontSize = 16.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
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
            NewsImage(
                imageUrl = article.imageUrl,
                contentDescription = article.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )

            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = Constants.getCategoryDisplayName(article.category),
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
