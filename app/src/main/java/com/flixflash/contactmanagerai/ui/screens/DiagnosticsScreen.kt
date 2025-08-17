package com.flixflash.contactmanagerai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flixflash.contactmanagerai.data.network.CallerIdApi
import com.flixflash.contactmanagerai.data.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

@HiltViewModel
class DiagnosticsViewModel @Inject constructor(
    private val settings: SettingsRepository,
    private val callerIdApi: CallerIdApi,
    private val okHttp: OkHttpClient
) : ViewModel() {
    var callerStatus by mutableStateOf<String>("-")
        private set
    var rasaStatus by mutableStateOf<String>("-")
        private set
    var piperStatus by mutableStateOf<String>("-")
        private set
    var voskStatus by mutableStateOf<String>("-")
        private set

    fun runAll() {
        viewModelScope.launch { testCaller() }
        viewModelScope.launch { testRasa() }
        viewModelScope.launch { testPiper() }
        viewModelScope.launch { testVosk() }
    }

    private suspend fun testCaller() {
        callerStatus = "..."
        runCatching { callerIdApi.health() }
            .onSuccess { callerStatus = "OK" }
            .onFailure { callerStatus = "FAIL: ${it.message}" }
    }

    private suspend fun testRasa() {
        rasaStatus = "..."
        val base = settings.settingsFlow.valueOrNull()?.rasaUrl ?: return run { rasaStatus = "N/A" }
        runCatching {
            val req = Request.Builder().url(base).build()
            okHttp.newCall(req).execute().use { /* just reachability */ }
        }.onSuccess { rasaStatus = "Reachable" }
            .onFailure { rasaStatus = "FAIL: ${it.message}" }
    }

    private suspend fun testPiper() {
        piperStatus = "..."
        val base = settings.settingsFlow.valueOrNull()?.piperUrl ?: return run { piperStatus = "N/A" }
        runCatching {
            val url = if (base.endsWith("/")) base + "synthesize" else "$base/synthesize"
            val req = Request.Builder().url(url).post(okhttp3.RequestBody.create("text/plain".toMediaTypeOrNull(), "ping")).build()
            okHttp.newCall(req).execute().use { /* status is enough */ }
        }.onSuccess { piperStatus = "OK" }
            .onFailure { piperStatus = "FAIL: ${it.message}" }
    }

    private suspend fun testVosk() {
        voskStatus = "..."
        val ws = settings.settingsFlow.valueOrNull()?.voskWsUrl ?: return run { voskStatus = "N/A" }
        runCatching {
            val request = Request.Builder().url(ws).build()
            val latch = java.util.concurrent.CountDownLatch(1)
            val listener = object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) { latch.countDown(); webSocket.cancel() }
                override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) { latch.countDown() }
            }
            okHttp.newWebSocket(request, listener)
            latch.await()
        }.onSuccess { voskStatus = "Reachable" }
            .onFailure { voskStatus = "FAIL: ${it.message}" }
    }
}

@Composable
fun DiagnosticsScreen() {
    val vm: DiagnosticsViewModel = hiltViewModel()
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("التشخيص", style = MaterialTheme.typography.titleLarge)
        Button(onClick = { vm.runAll() }) { Text("تشغيل الفحص") }
        ListItem(headlineText = { Text("CallerID API") }, supportingText = { Text(vm.callerStatus) })
        ListItem(headlineText = { Text("Rasa") }, supportingText = { Text(vm.rasaStatus) })
        ListItem(headlineText = { Text("Piper") }, supportingText = { Text(vm.piperStatus) })
        ListItem(headlineText = { Text("Vosk") }, supportingText = { Text(vm.voskStatus) })
    }
}