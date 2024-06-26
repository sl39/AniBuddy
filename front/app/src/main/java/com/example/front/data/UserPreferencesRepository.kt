package com.example.front.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>){
    private object PreferencesKeys{
        val accessToken : Preferences.Key<String> = stringPreferencesKey("accessToken")
        val refreshToken : Preferences.Key<String> = stringPreferencesKey("refreshToken")
    }

    val getAccessToken: Flow<String> = dataStore.data
        .catch { e ->
            if(e is IOException){
                emit(emptyPreferences())
            } else{
                throw e
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.accessToken] ?: ""
        }

    val getRefreshToken: Flow<String> = dataStore.data
        .catch { e ->
            if(e is IOException){
                emit(emptyPreferences())
            } else{
                throw e
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.refreshToken] ?: ""
        }

    suspend fun setAccessandRefreshToken(accessToken: String,refreshToken: String){
        dataStore.edit {
            preferences ->
            preferences[PreferencesKeys.accessToken] = accessToken
            preferences[PreferencesKeys.refreshToken] = refreshToken

        }
    }

}