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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SmsViewModel @Inject constructor(
	private val spamEngine: SpamMLEngine
) : ViewModel() {
	var messages by mutableStateOf(listOf<SmsItem>())
		private set
	var hideSpam by mutableStateOf(true)
	var loading by mutableStateOf(false)

	fun load(context: android.content.Context) {
		viewModelScope.launch {
			loading = true
			val list = withContext(Dispatchers.IO) { readSms(context) }
			val classified = list.map { item ->
				viewModelScope.launch {
					val res = spamEngine.classifyMessageWithFallback(item.body)
					item.isSpam = res.first
				}
				item
			}
			messages = classified
			loading = false
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
		}
		if (vm.loading) LinearProgressIndicator(Modifier.fillMaxWidth())
		LazyColumn(Modifier.fillMaxSize()) {
			items(vm.messages.filter { if (vm.hideSpam) !it.isSpam else true }) { msg ->
				Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
					Column(Modifier.padding(8.dp)) {
						Text(msg.address, style = MaterialTheme.typography.titleSmall)
						Text(msg.body, maxLines = 3)
						if (msg.isSpam) Text("Spam", color = MaterialTheme.colorScheme.error)
					}
				}
			}
		}
	}
}