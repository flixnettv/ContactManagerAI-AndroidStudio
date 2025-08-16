package com.flixflash.contactmanagerai.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import com.flixflash.contactmanagerai.data.network.RasaApi
import com.flixflash.contactmanagerai.data.network.RasaMessageRequest
import com.flixflash.contactmanagerai.data.settings.SettingsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiCallRepository @Inject constructor(
    private val rasaApi: RasaApi,
    private val settings: SettingsRepository,
    private val client: OkHttpClient
) {
    suspend fun sendToRasa(sender: String, message: String): List<String> = withContext(Dispatchers.IO) {
        val res = rasaApi.sendMessage(RasaMessageRequest(sender, message))
        res.mapNotNull { it.text }
    }

    suspend fun ttsSpeak(text: String): ByteArray = withContext(Dispatchers.IO) {
        val base = settings.settingsFlow.first().piperUrl
        val url = if (base.endsWith("/")) base + "synthesize" else "$base/synthesize"
        val req = Request.Builder()
            .url(url)
            .post(text.toRequestBody("text/plain".toMediaType()))
            .build()
        client.newCall(req).execute().use { resp ->
            if (!resp.isSuccessful) throw IllegalStateException("TTS failed: ${resp.code}")
            resp.body?.bytes() ?: ByteArray(0)
        }
    }
}