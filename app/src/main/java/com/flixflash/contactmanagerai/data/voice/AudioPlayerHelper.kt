package com.flixflash.contactmanagerai.data.voice

import android.content.Context
import android.media.MediaPlayer
import java.io.File

object AudioPlayerHelper {
    private var player: MediaPlayer? = null

    fun playWav(context: Context, data: ByteArray) {
        stop()
        val tmp = File.createTempFile("tts_", ".wav", context.cacheDir)
        tmp.outputStream().use { it.write(data) }
        player = MediaPlayer().apply {
            setDataSource(tmp.absolutePath)
            setOnCompletionListener { stop(); tmp.delete() }
            prepare()
            start()
        }
    }

    fun stop() {
        try {
            player?.stop()
            player?.release()
        } catch (_: Exception) {}
        player = null
    }
}