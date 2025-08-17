package com.flixflash.contactmanagerai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flixflash.contactmanagerai.data.network.BlockEntry
import com.flixflash.contactmanagerai.data.repository.CallerIdRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlocklistViewModel @Inject constructor(
    private val repo: CallerIdRepository
) : ViewModel() {
    var entries by mutableStateOf(listOf<BlockEntry>())
        private set
    var loading by mutableStateOf(false)

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            loading = true
            runCatching { repo.getBlocklist() }
                .onSuccess { entries = it }
            loading = false
        }
    }

    fun add(number: String, note: String?) {
        viewModelScope.launch {
            runCatching { repo.addBlock(number, note) }
            refresh()
        }
    }

    fun remove(number: String) {
        viewModelScope.launch {
            runCatching { repo.removeBlock(number) }
            refresh()
        }
    }
}

@Composable
fun BlocklistScreen() {
    val vm: BlocklistViewModel = hiltViewModel()
    var number by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("قائمة الحظر", style = MaterialTheme.typography.titleLarge)
        if (vm.loading) LinearProgressIndicator(Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = number, onValueChange = { number = it }, label = { Text("رقم") })
            OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("ملاحظة") })
            Button(onClick = { vm.add(number, note); number = ""; note = "" }) { Text("إضافة") }
        }
        Divider(Modifier.padding(vertical = 8.dp))
        LazyColumn(Modifier.fillMaxSize()) {
            items(vm.entries) { e ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(Modifier.weight(1f)) {
                            Text(e.number, style = MaterialTheme.typography.titleSmall)
                            if (!e.note.isNullOrBlank()) Text(e.note!!)
                        }
                        Button(onClick = { vm.remove(e.number) }) { Text("حذف") }
                    }
                }
            }
        }
    }
}