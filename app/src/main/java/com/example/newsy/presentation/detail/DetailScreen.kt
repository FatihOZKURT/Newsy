package com.example.newsy.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    articleId: String,
    viewModel: DetailViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(articleId) {
        viewModel.loadArticle(articleId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("News Detail", fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        val article = uiState.article
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (article != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Image Placeholder for ID: ${article.id}")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = article.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 28.sp
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Burada haberin detaylı içeriği yer alacak. Navigation 3 ve ViewModel ile başarılı bir şekilde geçiş yapıldı. " +
                            "Bu ekran haberin tüm detaylarını, görsellerini ve ilgili diğer bilgileri gösterecek şekilde tasarlanacaktır.",
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    lineHeight = 24.sp
                )
            }
        }
    }
}
