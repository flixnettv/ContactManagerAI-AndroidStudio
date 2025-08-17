package com.flixflash.contactmanagerai.data.voice

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import okhttp3.*
import okio.ByteString
import org.json.JSONObject

class VoskSttClient(private val wsUrl: String) {
    private var webSocket: WebSocket? = null
    private var audioRecord: AudioRecord? = null
    private var recordJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _transcripts = MutableSharedFlow<String>(extraBufferCapacity = 64)
    val transcripts: SharedFlow<String> = _transcripts

    fun start() {
        val client = OkHttpClient()
        val request = Request.Builder().url(wsUrl).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                startRecording()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val json = JSONObject(text)
                    val partial = json.optString("partial")
                    val full = json.optString("text")
                    val out = if (full.isNotEmpty()) full else partial
                    if (out.isNotEmpty()) scope.launch { _transcripts.emit(out) }
                } catch (_: Exception) {}
            }
        })
    }

    private fun startRecording() {
        val sampleRate = 16000
        val channelConfig = AudioFormat.CHANNEL_IN_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        val minBuf = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channelConfig,
            audioFormat,
            minBuf
        )
        audioRecord?.startRecording()
        recordJob = scope.launch {
            val buffer = ByteArray(minBuf)
            while (audioRecord?.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                if (read > 0) {
                    webSocket?.send(ByteString.of(buffer, 0, read))
                }
            }
        }
    }

    fun stop() {
        try {
            audioRecord?.stop()
            audioRecord?.release()
        } catch (_: Exception) {}
        audioRecord = null
        try {
            recordJob?.cancel()
        } catch (_: Exception) {}
        recordJob = null
        try {
            webSocket?.close(1000, "bye")
        } catch (_: Exception) {}
        webSocket = null
    }
}