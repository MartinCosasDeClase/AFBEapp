package com.example.afbe.preferences


import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val TOKEN_KEY = stringPreferencesKey("jwt_token")
        val USER_KEY = stringPreferencesKey("user_data")
        val FCM_TOKEN_KEY = stringPreferencesKey("fcm_token")
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    suspend fun getToken(): String? {
        return context.dataStore.data
            .map { prefs -> prefs[TOKEN_KEY] }
            .first()
    }

    suspend fun clearToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
        }
    }


    suspend fun saveUserNif(nif: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_KEY] = nif
        }
    }


    suspend fun getUserNif(): String? {
        return context.dataStore.data.map { prefs ->
            prefs[USER_KEY]
        }.first()
    }

    suspend fun saveFcmToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[FCM_TOKEN_KEY] = token
        }
    }

    suspend fun getFcmToken(): String? {
        return context.dataStore.data
            .map { prefs -> prefs[FCM_TOKEN_KEY] }
            .first()
    }
}