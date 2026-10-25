package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.DarkThemeVariant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.snPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "sn_creativity_preferences"
)

data class StoredWorkspacePreferences(
    val darkThemeVariant: DarkThemeVariant = DarkThemeVariant.OBSIDIAN_PURPLE,
    val neonGlowEnabled: Boolean = true,
    val lowercaseHashtags: Boolean = false,
    val includeScriptCues: Boolean = true
)

class ThemePreferencesRepository(private val context: Context) {

    private object Keys {
        val DARK_THEME_VARIANT = stringPreferencesKey("dark_theme_variant")
        val NEON_GLOW_ENABLED = booleanPreferencesKey("neon_glow_enabled")
        val LOWERCASE_HASHTAGS = booleanPreferencesKey("lowercase_hashtags")
        val INCLUDE_SCRIPT_CUES = booleanPreferencesKey("include_script_cues")
    }

    val preferencesFlow: Flow<StoredWorkspacePreferences> =
        context.snPreferencesDataStore.data.map { prefs ->
            StoredWorkspacePreferences(
                darkThemeVariant = DarkThemeVariant.fromKey(prefs[Keys.DARK_THEME_VARIANT]),
                neonGlowEnabled = prefs[Keys.NEON_GLOW_ENABLED] ?: true,
                lowercaseHashtags = prefs[Keys.LOWERCASE_HASHTAGS] ?: false,
                includeScriptCues = prefs[Keys.INCLUDE_SCRIPT_CUES] ?: true
            )
        }

    suspend fun setDarkThemeVariant(variant: DarkThemeVariant) {
        context.snPreferencesDataStore.edit { prefs ->
            prefs[Keys.DARK_THEME_VARIANT] = variant.storageKey
        }
    }

    suspend fun setNeonGlowEnabled(enabled: Boolean) {
        context.snPreferencesDataStore.edit { prefs ->
            prefs[Keys.NEON_GLOW_ENABLED] = enabled
        }
    }

    suspend fun setLowercaseHashtags(enabled: Boolean) {
        context.snPreferencesDataStore.edit { prefs ->
            prefs[Keys.LOWERCASE_HASHTAGS] = enabled
        }
    }

    suspend fun setIncludeScriptCues(enabled: Boolean) {
        context.snPreferencesDataStore.edit { prefs ->
            prefs[Keys.INCLUDE_SCRIPT_CUES] = enabled
        }
    }
}
