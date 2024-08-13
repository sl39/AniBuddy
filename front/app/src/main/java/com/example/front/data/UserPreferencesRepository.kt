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

class UserPreferencesRepository(private val dataStore:DataStore<Preferences>){
    private object PreferencesKeys{
        val accessToken : Preferences.Key<String> = stringPreferencesKey("accessToken")
        val refreshToken : Preferences.Key<String> = stringPreferencesKey("refreshToken")
        val userType : Preferences.Key<String> = stringPreferencesKey("userType")
    }
    val getAccessToken: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.accessToken] ?: ""
        }
    val getRefreshToken: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.refreshToken] ?: ""
        }

    suspend fun setAccessToken(accessToken : String){
        dataStore.edit {
            preferences ->
            preferences[PreferencesKeys.accessToken] = accessToken
        }
    }

    suspend fun setRefreshToken(refreshToken : String){
        dataStore.edit {
                preferences ->
            preferences[PreferencesKeys.refreshToken] = refreshToken
        }
    }
    val getUserType: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.userType] ?: ""
        }

    suspend fun setUserType(userType : String){
        dataStore.edit {
                preferences ->
            preferences[PreferencesKeys.userType] = userType
        }
    }
}
