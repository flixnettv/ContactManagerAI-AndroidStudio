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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdvancedSettingsViewModel @Inject constructor(
	private val repo: SettingsRepository
) : ViewModel() {
	var state by mutableStateOf(SettingsRepository.Settings())
		private set

	init {
		viewModelScope.launch { repo.settingsFlow.collectLatest { state = it } }
	}

	fun save(newState: SettingsRepository.Settings) {
		viewModelScope.launch { repo.update { newState } }
	}
}

@Composable
fun AdvancedSettingsScreen() {
	val vm: AdvancedSettingsViewModel = hiltViewModel()
	var useBackend by remember(vm.state.useBackendSpam) { mutableStateOf(vm.state.useBackendSpam) }
	var callerId by remember(vm.state.callerIdUrl) { mutableStateOf(vm.state.callerIdUrl) }
	var rasa by remember(vm.state.rasaUrl) { mutableStateOf(vm.state.rasaUrl) }
	var piper by remember(vm.state.piperUrl) { mutableStateOf(vm.state.piperUrl) }
	var vosk by remember(vm.state.voskWsUrl) { mutableStateOf(vm.state.voskWsUrl) }

	Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
		Text("الإعدادات المتقدمة", style = MaterialTheme.typography.titleLarge)
		Divider()
		Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
			Text("استخدم تصنيف Spam عبر الخادم")
			Switch(checked = useBackend, onCheckedChange = { useBackend = it })
		}
		OutlinedTextField(value = callerId, onValueChange = { callerId = it }, label = { Text("CallerID URL") })
		OutlinedTextField(value = rasa, onValueChange = { rasa = it }, label = { Text("Rasa URL") })
		OutlinedTextField(value = piper, onValueChange = { piper = it }, label = { Text("Piper URL") })
		OutlinedTextField(value = vosk, onValueChange = { vosk = it }, label = { Text("Vosk WS URL") })
		Spacer(Modifier.height(12.dp))
		Button(onClick = { vm.save(SettingsRepository.Settings(useBackend, callerId, rasa, piper, vosk)) }) { Text("حفظ") }
	}
}