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
import kotlin.math.roundToInt

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "widget_preferences")

object WidgetPreferences {

    private val KEY_USE_COLORFUL = booleanPreferencesKey("use_colorful")
    private val KEY_BACKGROUND_ALPHA = floatPreferencesKey("background_alpha")
    private val KEY_CORNER_RADIUS = floatPreferencesKey("corner_radius")
    private val KEY_PADDING = floatPreferencesKey("padding")
    private val KEY_VERTICAL_PADDING = floatPreferencesKey("vertical_padding")
    private val KEY_HORIZONTAL_PADDING = floatPreferencesKey("horizontal_padding")
    private val KEY_SHOW_APP_NAME = booleanPreferencesKey("show_app_name")

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

    fun getCornerRadius(dataStore: Preferences?) = dataStore?.get(KEY_CORNER_RADIUS) ?: 60f

    suspend fun setCornerRadius(context: Context, radius: Float) {
        context.dataStore.edit { preferences ->
            preferences[KEY_CORNER_RADIUS] = radius.coerceIn(0f, 60f)
        }
    }

    fun getVerticalPadding(dataStore: Preferences?): Float =
        dataStore?.get(KEY_VERTICAL_PADDING)?.roundToInt()?.toFloat() ?: 0f

    suspend fun setVerticalPadding(context: Context, padding: Float) {
        context.dataStore.edit { preferences ->
            preferences[KEY_VERTICAL_PADDING] = padding.coerceIn(0f, 24f)
        }
    }

    fun getHorizontalPadding(dataStore: Preferences?): Float =
        dataStore?.get(KEY_HORIZONTAL_PADDING)?.roundToInt()?.toFloat() ?: 0f

    suspend fun setHorizontalPadding(context: Context, padding: Float) {
        context.dataStore.edit { preferences ->
            preferences[KEY_HORIZONTAL_PADDING] = padding.coerceIn(0f, 24f)
        }
    }

    fun showAppName(dataStore: Preferences?) = dataStore?.get(KEY_SHOW_APP_NAME) ?: false

    suspend fun setShowAppName(context: Context, showAppName: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SHOW_APP_NAME] = showAppName
        }
    }
}

