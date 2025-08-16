package com.flixflash.contactmanagerai.data.settings

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.flixflash.contactmanager.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "app_settings")

@Singleton
class SettingsRepository @Inject constructor(
	@ApplicationContext private val context: Context
) {
	companion object {
		private val USE_BACKEND_SPAM = booleanPreferencesKey("use_backend_spam")
		private val CALLERID_URL = stringPreferencesKey("callerid_url")
		private val RASA_URL = stringPreferencesKey("rasa_url")
		private val PIPER_URL = stringPreferencesKey("piper_url")
		private val VOSK_WS = stringPreferencesKey("vosk_ws")
	}

	data class Settings(
		val useBackendSpam: Boolean = true,
		val callerIdUrl: String = BuildConfig.CALLERID_BASE_URL,
		val rasaUrl: String = BuildConfig.RASA_BASE_URL,
		val piperUrl: String = BuildConfig.PIPER_BASE_URL,
		val voskWsUrl: String = BuildConfig.VOSK_WS_URL,
	)

	val settingsFlow: Flow<Settings> = context.dataStore.data.map { p ->
		Settings(
			useBackendSpam = p[USE_BACKEND_SPAM] ?: true,
			callerIdUrl = p[CALLERID_URL] ?: BuildConfig.CALLERID_BASE_URL,
			rasaUrl = p[RASA_URL] ?: BuildConfig.RASA_BASE_URL,
			piperUrl = p[PIPER_URL] ?: BuildConfig.PIPER_BASE_URL,
			voskWsUrl = p[VOSK_WS] ?: BuildConfig.VOSK_WS_URL,
		)
	}

	suspend fun update(block: Settings.() -> Settings) {
		context.dataStore.edit { prefs ->
			val current = Settings(
				useBackendSpam = prefs[USE_BACKEND_SPAM] ?: true,
				callerIdUrl = prefs[CALLERID_URL] ?: BuildConfig.CALLERID_BASE_URL,
				rasaUrl = prefs[RASA_URL] ?: BuildConfig.RASA_BASE_URL,
				piperUrl = prefs[PIPER_URL] ?: BuildConfig.PIPER_BASE_URL,
				voskWsUrl = prefs[VOSK_WS] ?: BuildConfig.VOSK_WS_URL,
			)
			val next = current.block()
			prefs[USE_BACKEND_SPAM] = next.useBackendSpam
			prefs[CALLERID_URL] = next.callerIdUrl
			prefs[RASA_URL] = next.rasaUrl
			prefs[PIPER_URL] = next.piperUrl
			prefs[VOSK_WS] = next.voskWsUrl
		}
	}
}