package com.example.newsy.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        private val INTERESTS_SELECTED = booleanPreferencesKey("interests_selected")
        private val SELECTED_CATEGORIES = stringSetPreferencesKey("selected_categories")
    }

    val isInterestsSelected: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[INTERESTS_SELECTED] ?: false
        }

    val selectedCategories: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_CATEGORIES] ?: emptySet()
        }

    suspend fun setInterestsSelected(selected: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[INTERESTS_SELECTED] = selected
        }
    }

    suspend fun saveSelectedCategories(categories: Set<String>) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_CATEGORIES] = categories
        }
    }
}
