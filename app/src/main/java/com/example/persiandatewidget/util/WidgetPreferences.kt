package com.example.persiandatewidget.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "widget_preferences")

object WidgetPreferences {

    private val KEY_USE_COLORFUL = booleanPreferencesKey("use_colorful")
    private val KEY_BACKGROUND_ALPHA = floatPreferencesKey("background_alpha")

    @Composable
    fun dataStore(context: Context): State<Preferences?> {
        val initialData = runBlocking { context.dataStore.data.firstOrNull() }
        return context.dataStore.data.collectAsState(initialData)
    }

    fun isColorful(dataStore: Preferences?) = dataStore?.get(KEY_USE_COLORFUL) ?: true

    suspend fun setColorful(context: Context, useColorful: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USE_COLORFUL] = useColorful
        }
    }

    fun getBackgroundAlpha(dataStore: Preferences?) = dataStore?.get(KEY_BACKGROUND_ALPHA) ?: 1.0f

    suspend fun setBackgroundAlpha(context: Context, alpha: Float) {
        context.dataStore.edit { preferences ->
            preferences[KEY_BACKGROUND_ALPHA] = alpha.coerceIn(0f, 1f)
        }
    }
}

