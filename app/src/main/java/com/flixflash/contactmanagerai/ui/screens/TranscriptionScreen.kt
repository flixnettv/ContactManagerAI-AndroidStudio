package com.flixflash.contactmanagerai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flixflash.contactmanagerai.data.settings.SettingsRepository
import com.flixflash.contactmanagerai.data.voice.VoskSttClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranscriptionViewModel @Inject constructor(
    private val settings: SettingsRepository
) : ViewModel() {
    var lines by mutableStateOf(listOf<String>())
        private set
    var running by mutableStateOf(false)
    private var stt: VoskSttClient? = null
    private var job: Job? = null

    fun toggle() {
        if (running) stop() else start()
    }

    private fun start() {
        viewModelScope.launch {
            val ws = settings.settingsFlow.collect { }
        }
    }

    fun startWithUrl(url: String) {
        stt = VoskSttClient(url)
        running = true
        job = viewModelScope.launch {
            stt!!.transcripts.collectLatest { t ->
                lines = lines + t
            }
        }
        stt!!.start()
    }

    fun stop() {
        stt?.stop(); stt = null
        job?.cancel(); job = null
        running = false
    }
}

@Composable
fun TranscriptionScreen(vm: TranscriptionViewModel = hiltViewModel()) {
    val scope = rememberCoroutineScope()
    var url by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("النسخ الحي", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = url, onValueChange = { url = it }, label = { Text("Vosk WS URL") })
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { vm.startWithUrl(url) }, enabled = !vm.running) { Text("بدء") }
            Button(onClick = { vm.stop() }, enabled = vm.running) { Text("إيقاف") }
        }
        Divider(Modifier.padding(vertical = 8.dp))
        vm.lines.takeLast(50).forEach { Text(it) }
    }
}