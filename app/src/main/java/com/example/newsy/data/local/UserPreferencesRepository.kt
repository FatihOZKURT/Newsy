package com.example.newsy.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        private val INTERESTS_SELECTED = booleanPreferencesKey("interests_selected")
    }

    val isInterestsSelected: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[INTERESTS_SELECTED] ?: false
        }

    suspend fun setInterestsSelected(selected: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[INTERESTS_SELECTED] = selected
        }
    }
}
