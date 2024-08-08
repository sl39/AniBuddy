package com.example.front.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token_preferences")

object preferencesRepository {
    @Volatile
    private var Instance: UserPreferencesRepository? = null

    fun getUserPreferencesRepository(context: Context): UserPreferencesRepository {
        return Instance ?: synchronized(this) {
            Instance ?: UserPreferencesRepository(context.dataStore).also { Instance = it }
        }
    }
}