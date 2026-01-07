package com.shannon.shannonweek14.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class TokenManager(private val context: Context) {

    companion object {
        val TOKEN_KEY = stringPreferencesKey("auth_token")

        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
    }

    val contextDataStore = context.dataStore
    suspend fun saveToken(token: String, role: String) {
        contextDataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[USER_ROLE_KEY] = role
        }
    }

    val role: Flow<String?> = contextDataStore.data.map { preferences ->
        preferences[USER_ROLE_KEY]
    }

    suspend fun getToken(): String? {
        val prefs = context.dataStore.data.first()
        return prefs[TOKEN_KEY]
    }
}
