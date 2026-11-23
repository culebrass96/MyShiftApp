package com.example.myshiftapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getDatabase(this)
        val dao = db.shiftDao()

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ShiftScreen(dao)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShiftScreen(dao: ShiftDao) {
    val shifts by dao.getAllShifts().collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Мой график") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    ) { padding ->
        if (shifts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Нет смен. Нажмите + чтобы добавить.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(shifts) { shift ->
                    ShiftCard(shift)
                }
            }
        }
    }

    if (showDialog) {
        AddShiftDialog(
            onDismiss = { showDialog = false },
            onAdd = { date, type, time ->
                scope.launch {
                    dao.insertShift(ShiftEntity(date = date, type = type, time = time))
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun ShiftCard(shift: ShiftEntity) {
    val color = when (shift.type) {
        "День" -> Color(0xFF4CAF50)
        "Ночь" -> Color(0xFF673AB7)
        "Отсыпной" -> Color(0xFFFF9800)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = shift.date, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Text(text = shift.type, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = color)
            }
            if (shift.time.isNotEmpty()) {
                Text(text = shift.time, fontWeight = FontWeight.Medium, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun AddShiftDialog(onDismiss: () -> Unit, onAdd: (String, String, String) -> Unit) {
    var date by remember { mutableStateOf("2026-11-24") }
    var type by remember { mutableStateOf("День") }
    var time by remember { mutableStateOf("08:00 - 20:00") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новая смена") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Дата (ГГГГ-ММ-ДД)") })
                OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Тип (День/Ночь/Выходной)") })
                OutlinedTextField(value = time, onValueChange = { time = it }, label = { Text("Время (08:00 - 20:00)") })
            }
        },
        confirmButton = {
            TextButton(onClick = { onAdd(date, type, time) }) { Text("Добавить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}
