package com.example.newsy.presentation.interests

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newsy.domain.model.Interest
import com.example.newsy.ui.theme.Black
import com.example.newsy.ui.theme.Blue
import com.example.newsy.ui.theme.NewsyTheme
import com.example.newsy.ui.theme.Red
import com.example.newsy.ui.theme.White

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InterestsScreen(
    viewModel: InterestsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Black,
        bottomBar = {
            Button(
                onClick = { /* Start clicked */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Red),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Start",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "Tailor your feed to what matters most to you",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = White,
                lineHeight = 40.sp,
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Don't worry, you can change these preferences at any time in the app settings.",
                fontSize = 14.sp,
                color = White.copy(alpha = 0.6f),
                lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth(0.85f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    uiState.interests.forEachIndexed { index, interest ->
                        InterestTag(
                            interest = interest,
                            onClick = {
                                viewModel.toggleInterest(index)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun InterestTag(
    interest: Interest,
    onClick: () -> Unit
) {
    val backgroundColor = if (interest.isSelected) Blue else Color.Transparent
    val borderColor = if (interest.isSelected) Blue else White.copy(alpha = 0.5f)
    val icon = if (interest.isSelected) Icons.Default.Check else Icons.Default.Add

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = interest.name,
                color = White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun InterestsScreenPreview() {
    NewsyTheme {
        InterestsScreen()
    }
}
