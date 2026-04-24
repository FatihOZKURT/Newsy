package com.example.newsy

import android.app.Application
import com.example.newsy.data.local.UserPreferencesRepository
import com.example.newsy.data.remote.api.GuardianApiService
import com.example.newsy.navigation.NavigationViewModel
import com.example.newsy.presentation.home.HomeViewModel
import com.example.newsy.presentation.interests.InterestsViewModel
import com.example.newsy.presentation.detail.DetailViewModel
import com.example.newsy.data.repository.NewsRepositoryImpl
import com.example.newsy.domain.repository.NewsRepository
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.singleOf

class NewsyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NewsyApplication)
            modules(appModule)
        }
    }
}

val appModule = module {
    // Network
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }
        }
    }
    singleOf(::GuardianApiService)
    single<NewsRepository> { NewsRepositoryImpl(get()) }

    // Local
    singleOf(::UserPreferencesRepository)

    // ViewModels
    viewModel { InterestsViewModel(get()) }
    viewModel { HomeViewModel() }
    viewModel { DetailViewModel() }
    viewModel { NavigationViewModel(get()) }
}
