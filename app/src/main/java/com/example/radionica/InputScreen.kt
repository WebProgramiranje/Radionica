package com.example.radionica

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun InputScreen(
    a: String, onAChange: (String) -> Unit,
    b: String, onBChange: (String) -> Unit,
    c: String, onCChange: (String) -> Unit,
    onCalculate: (Float, Float, Float) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp, top = 96.dp, end = 24.dp, bottom = 96.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Površina trougla", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))

        TriangleInput("Stranica a (m)", a, onAChange)
        TriangleInput("Stranica b (m)", b, onBChange)
        TriangleInput("Stranica c (m)", c, onCChange)

        Button(
            onClick = {
                val fA = a.toFloatOrNull() ?: 0f
                val fB = b.toFloatOrNull() ?: 0f
                val fC = c.toFloatOrNull() ?: 0f
                if (fA + fB > fC && fA + fC > fB && fB + fC > fA) {
                    onCalculate(fA, fB, fC)
                }
                else {
                    showDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Izračunaj površinu", style = MaterialTheme.typography.titleLarge )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Greška") },
            text = { Text("Unete stranice ne mogu da formiraju trougao.") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("U redu", style = MaterialTheme.typography.labelLarge)
                }
            }
        )
    }
}

@Composable
fun TriangleInput(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = MaterialTheme.typography.titleLarge) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewInputScreen() {
    MaterialTheme {
        InputScreen("3", {}, "4", {}, "5", {}, { _, _, _ -> })
    }
}