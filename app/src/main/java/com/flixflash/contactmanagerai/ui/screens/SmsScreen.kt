package com.flixflash.contactmanagerai.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.provider.Telephony
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flixflash.spamdetection.SpamMLEngine
import com.flixflash.contactmanagerai.data.settings.SettingsRepository
import com.flixflash.contactmanagerai.data.repository.CallerIdRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SmsViewModel @Inject constructor(
    private val spamEngine: SpamMLEngine,
    private val settings: SettingsRepository,
    private val callerRepo: CallerIdRepository
) : ViewModel() {
    var messages by mutableStateOf(listOf<SmsItem>())
        private set
    var hideSpam by mutableStateOf(true)
    var loading by mutableStateOf(false)
    var useBackend by mutableStateOf(true)
    var conversationsMode by mutableStateOf(true)
    private val reported = mutableStateListOf<String>()

    fun load(context: android.content.Context) {
        viewModelScope.launch {
            loading = true
            useBackend = settings.settingsFlow.first().useBackendSpam
            val list = withContext(Dispatchers.IO) { readSms(context) }
            val classified = list.map { item ->
                viewModelScope.launch {
                    val res = if (useBackend) spamEngine.classifyMessageWithFallback(item.body)
                    else spamEngine.classifyMessageLocal(item.body)
                    item.isSpam = res.first
                }
                item
            }
            messages = classified
            loading = false
        }
    }

    fun grouped(): List<ConversationItem> {
        val groups = messages.groupBy { it.address }
        return groups.map { (addr, list) ->
            val sorted = list.sortedByDescending { it.date }
            val last = sorted.firstOrNull()
            val spamCount = sorted.count { it.isSpam }
            ConversationItem(address = addr, lastBody = last?.body ?: "", count = sorted.size, spamCount = spamCount)
        }.sortedByDescending { it.count }
    }

    fun isReported(number: String): Boolean = reported.contains(number)

    fun reportSpam(number: String) {
        if (number.isBlank()) return
        if (reported.contains(number)) return
        viewModelScope.launch {
            runCatching { callerRepo.reportSpam(number, "sms_user_report") }
            reported.add(number)
        }
    }

    private fun readSms(context: android.content.Context): List<SmsItem> {
        val list = mutableListOf<SmsItem>()
        val uri = Telephony.Sms.Inbox.CONTENT_URI
        val projection = arrayOf(Telephony.Sms.ADDRESS, Telephony.Sms.BODY, Telephony.Sms.DATE)
        context.contentResolver.query(uri, projection, null, null, Telephony.Sms.DEFAULT_SORT_ORDER)?.use { cursor ->
            val idxAddr = cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)
            val idxBody = cursor.getColumnIndexOrThrow(Telephony.Sms.BODY)
            val idxDate = cursor.getColumnIndexOrThrow(Telephony.Sms.DATE)
            while (cursor.moveToNext()) {
                list.add(
                    SmsItem(
                        address = cursor.getString(idxAddr) ?: "",
                        body = cursor.getString(idxBody) ?: "",
                        date = cursor.getLong(idxDate)
                    )
                )
            }
        }
        return list
    }
}

data class SmsItem(
    val address: String,
    val body: String,
    val date: Long,
    var isSpam: Boolean = false
)

data class ConversationItem(
    val address: String,
    val lastBody: String,
    val count: Int,
    val spamCount: Int
)

@Composable
fun SmsScreen() {
    val vm: SmsViewModel = hiltViewModel()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            vm.load(context)
        }
    }
    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("إخفاء المزعج")
            Switch(checked = vm.hideSpam, onCheckedChange = { vm.hideSpam = it })
            Spacer(Modifier.width(8.dp))
            Text("AI")
            Switch(checked = vm.useBackend, onCheckedChange = { vm.useBackend = it; vm.load(context) })
            Spacer(Modifier.width(8.dp))
            Text("المحادثات")
            Switch(checked = vm.conversationsMode, onCheckedChange = { vm.conversationsMode = it })
        }
        if (vm.loading) LinearProgressIndicator(Modifier.fillMaxWidth())
        if (vm.conversationsMode) {
            val convos = vm.grouped()
            LazyColumn(Modifier.fillMaxSize()) {
                items(convos) { c ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(Modifier.padding(8.dp)) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text(c.address, style = MaterialTheme.typography.titleSmall)
                                if (c.spamCount > 0) Text("${c.spamCount} Spam", color = MaterialTheme.colorScheme.error)
                            }
                            Text(c.lastBody, maxLines = 2)
                            Text("${c.count} رسائل")
                        }
                    }
                }
            }
        } else {
            LazyColumn(Modifier.fillMaxSize()) {
                items(vm.messages.filter { if (vm.hideSpam) !it.isSpam else true }) { msg ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(Modifier.padding(8.dp)) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text(msg.address, style = MaterialTheme.typography.titleSmall)
                                if (vm.isReported(msg.address)) Text("مبلّغ", color = MaterialTheme.colorScheme.primary)
                            }
                            Text(msg.body, maxLines = 3)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (msg.isSpam) Text("Spam", color = MaterialTheme.colorScheme.error)
                                Button(onClick = { vm.reportSpam(msg.address) }, enabled = !vm.isReported(msg.address)) { Text("إبلاغ Spam") }
                            }
                        }
                    }
                }
            }
        }
    }
}