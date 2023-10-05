package com.example.bangkitnavigationapi.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AppThemeSettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val THEME_KEY = booleanPreferencesKey("theme_setting")

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting() {
        dataStore.edit { preferences ->
            val currentTheme = preferences[THEME_KEY] ?: false
            preferences[THEME_KEY] = !currentTheme
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppThemeSettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): AppThemeSettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = AppThemeSettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}