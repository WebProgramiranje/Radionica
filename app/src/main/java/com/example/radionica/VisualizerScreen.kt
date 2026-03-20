package com.example.radionica

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import kotlin.math.*

@Composable
fun VisualizerScreen(a: Float, b: Float, c: Float, onBack: () -> Unit) {
    val stranice = listOf("a" to a, "b" to b, "c" to c).sortedBy { it.second }
    val sA = stranice[0].second
    val sB = stranice[1].second
    val sC = stranice[2].second

    val aName = stranice[0].first
    val bName = stranice[1].first
    val cName = stranice[2].first

    val s = (a + b + c) / 2
    val povrsina = sqrt(s * (s - a) * (s - b) * (s - c))

    val h = (2 * povrsina) / sC
    val temp = sB.pow(2) - h.pow(2)
    val xTop = sqrt(if (temp < 0f) 0f else temp)

    val accentColor = MaterialTheme.colorScheme.primary
    val textMeasurer = rememberTextMeasurer()

    Column(modifier = Modifier.fillMaxSize().safeDrawingPadding().padding(all = 20.dp)) {
        Button(onClick = onBack) { Text("Nazad", style = MaterialTheme.typography.titleLarge) }

        Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val scale = minOf(size.width * 0.9f / sC, size.height * 0.9f / h)

                val trougaoScaledWidth = sC * scale
                val trougaoScaledHeight = h * scale

                val startX = (size.width - trougaoScaledWidth) / 2f
                val startY = (size.height + trougaoScaledHeight) / 2f

                val topX = startX + (xTop * scale)
                val topY = startY - (h * scale)
                val rightX = startX + (sC * scale)

                val path = Path().apply {
                    moveTo(startX, startY)
                    lineTo(rightX, startY)
                    lineTo(topX, topY)
                    close()
                }

                drawPath(path, color = accentColor, style = Stroke(width = 8f, join = StrokeJoin.Round))

                val aPosition = Offset((rightX + topX) / 2f + 24f, (startY + topY) / 2f - 56f)
                val bPosition = Offset((startX + topX) / 2f - 56f, (startY + topY) / 2f - 56f)
                val cPosition = Offset(startX + (sC * scale) / 2f, startY + 40f)

                val labelStyle = TextStyle(color = accentColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)

                drawText(textMeasurer = textMeasurer, text = aName, topLeft = aPosition, style = labelStyle)
                drawText(textMeasurer = textMeasurer, text = bName, topLeft = bPosition, style = labelStyle)
                drawText(textMeasurer = textMeasurer, text = cName, topLeft = cPosition, style = labelStyle)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Stranice: a = ${"%.1f".format(a)}m, b = ${"%.1f".format(b)}m, c = ${"%.1f".format(c)}m",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "Površina: ${"%.2f".format(povrsina)} m²",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

class TriangleDataProvider : PreviewParameterProvider<Triple<Float, Float, Float>> {
    override val values = sequenceOf(
        Triple(3f, 4f, 5f),
        Triple(5f, 5f, 5f),
        Triple(10f, 2f, 9f)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewVisualizer(
    @PreviewParameter(TriangleDataProvider::class) data: Triple<Float, Float, Float>
) {
    MaterialTheme {
        VisualizerScreen(data.first, data.second, data.third) {}
    }
}
