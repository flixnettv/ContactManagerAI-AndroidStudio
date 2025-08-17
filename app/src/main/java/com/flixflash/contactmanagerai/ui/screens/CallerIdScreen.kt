package com.flixflash.contactmanagerai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flixflash.contactmanagerai.data.repository.CallerIdRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallerIdViewModel @Inject constructor(
	private val repo: CallerIdRepository
): ViewModel() {
	var state by mutableStateOf(CallerIdState())
		private set

	fun lookup(number: String) {
		state = state.copy(loading = true, error = null)
		viewModelScope.launch {
			try {
				val info = repo.lookup(number)
				state = state.copy(loading = false, info = info)
			} catch (e: Exception) {
				state = state.copy(loading = false, error = e.message)
			}
		}
	}

	fun reportSpam(number: String) {
		viewModelScope.launch { runCatching { repo.reportSpam(number, "user_report") } }
	}
}

data class CallerIdState(
	val loading: Boolean = false,
	val info: com.flixflash.contactmanagerai.data.network.NumberInfo? = null,
	val error: String? = null
)

@Composable
fun CallerIdScreen() {
	val vm: CallerIdViewModel = hiltViewModel()
	var number by remember { mutableStateOf("") }
	val st = vm.state

	Column(Modifier.padding(16.dp)) {
		OutlinedTextField(value = number, onValueChange = { number = it }, label = { Text("رقم الهاتف") })
		Spacer(Modifier.height(8.dp))
		Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
			Button(onClick = { vm.lookup(number) }, enabled = !st.loading) { Text("استعلام") }
			Button(onClick = { vm.reportSpam(number) }, enabled = number.isNotBlank()) { Text("تبليغ Spam") }
		}
		Spacer(Modifier.height(12.dp))
		if (st.loading) LinearProgressIndicator()
		st.info?.let { info ->
			Card(Modifier.fillMaxWidth()) {
				Column(Modifier.padding(12.dp)) {
					Text("الاسم: ${info.name ?: "غير معروف"}")
					Text("النوع: ${info.type ?: "unknown"}")
					Text("المنطقة: ${info.location ?: "-"}")
					Text("نسبة Spam: ${info.spam_score}")
				}
			}
		}
		st.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
	}
}