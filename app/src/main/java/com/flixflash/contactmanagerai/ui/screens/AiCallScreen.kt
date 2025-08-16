package com.flixflash.contactmanagerai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flixflash.contactmanagerai.data.repository.AiCallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiCallViewModel @Inject constructor(
    private val repo: AiCallRepository
) : ViewModel() {
    var replies by mutableStateOf(listOf<String>())
        private set
    var loading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun send(sender: String, message: String) {
        loading = true; error = null
        viewModelScope.launch {
            runCatching { repo.sendToRasa(sender, message) }
                .onSuccess { replies = it }
                .onFailure { error = it.message }
            loading = false
        }
    }
}

@Composable
fun AiCallScreen() {
    val vm: AiCallViewModel = hiltViewModel()
    var phone by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("مكالمة AI", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("رقم الهاتف") })
        OutlinedTextField(value = reason, onValueChange = { reason = it }, label = { Text("سبب المكالمة") })
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { vm.send("user", "set_call_reason: $reason") }) { Text("إرسال للـ Rasa") }
        }
        if (vm.loading) LinearProgressIndicator()
        vm.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        vm.replies.forEach { Text("AI: $it") }
    }
}