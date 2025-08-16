package com.flixflash.contactmanagerai.data.voice

import com.flixflash.contactmanagerai.data.network.RasaApi
import com.flixflash.contactmanagerai.data.network.RasaMessageRequest
import com.flixflash.contactmanagerai.data.settings.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiConversationManager @Inject constructor(
	private val settings: SettingsRepository,
	private val rasa: RasaApi,
	private val http: OkHttpClient
) {
	private val scope = CoroutineScope(Dispatchers.IO)
	private var stt: VoskSttClient? = null
	private var sttJob: Job? = null
	private val ttsQueue = Channel<String>(capacity = Channel.UNLIMITED)
	private var ttsJob: Job? = null
	@Volatile private var running = false

	private val _audioOut = MutableSharedFlow<ByteArray>(extraBufferCapacity = 8)
	val audioOut: SharedFlow<ByteArray> = _audioOut

	fun isRunning(): Boolean = running

	fun start() {
		if (running) return
		running = true
		scope.launch {
			val cfg = settings.settingsFlow.first()
			stt = VoskSttClient(cfg.voskWsUrl)
			sttJob = scope.launch {
				stt!!.transcripts.collect { text ->
					val replies = try { rasa.sendMessage(RasaMessageRequest("user", text)) } catch (_: Exception) { emptyList() }
					replies.mapNotNull { it.text }.forEach { ttsQueue.trySend(it) }
				}
			}
			stt!!.start()
			ttsJob = scope.launch {
				for (text in ttsQueue) {
					try { playTts(cfg.piperUrl, text) } catch (_: Exception) { /* ignore */ }
					if (!running) break
				}
			}
		}
	}

	fun stop() {
		running = false
		try { stt?.stop() } catch (_: Exception) {}
		stt = null
		try { sttJob?.cancel() } catch (_: Exception) {}
		sttJob = null
		try { ttsJob?.cancel() } catch (_: Exception) {}
		ttsJob = null
		try { ttsQueue.close() } catch (_: Exception) {}
	}

	private fun playTts(base: String, text: String) {
		val url = if (base.endsWith("/")) base + "synthesize" else "$base/synthesize"
		val req = Request.Builder().url(url).post(text.toRequestBody("text/plain".toMediaType())).build()
		http.newCall(req).execute().use { resp ->
			val bytes = resp.body?.bytes() ?: return
			scope.launch { _audioOut.emit(bytes) }
		}
	}
}