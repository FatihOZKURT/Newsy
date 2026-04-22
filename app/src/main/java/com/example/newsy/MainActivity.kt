package com.example.newsy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.newsy.navigation.NewsyNavGraph
import com.example.newsy.ui.theme.NewsyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsyTheme {
                NewsyNavGraph()
            }
        }
    }
}

