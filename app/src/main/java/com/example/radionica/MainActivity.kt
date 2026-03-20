package com.example.radionica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.radionica.ui.theme.RadionicaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            RadionicaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Triangle()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Triangle() {
    val navController = rememberNavController()
    var sideA by remember { mutableStateOf("") }
    var sideB by remember { mutableStateOf("") }
    var sideC by remember { mutableStateOf("") }

    NavHost(navController = navController, startDestination = "input") {
        composable("input") {
            InputScreen(
                a = sideA, onAChange = { sideA = it },
                b = sideB, onBChange = { sideB = it },
                c = sideC, onCChange = { sideC = it }
            ) { a, b, c ->
                navController.navigate("draw/$a/$b/$c")
            }
        }
        composable(
            "draw/{a}/{b}/{c}",
            arguments = listOf(
                navArgument("a") { type = NavType.FloatType },
                navArgument("b") { type = NavType.FloatType },
                navArgument("c") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val a = backStackEntry.arguments?.getFloat("a") ?: 0f
            val b = backStackEntry.arguments?.getFloat("b") ?: 0f
            val c = backStackEntry.arguments?.getFloat("c") ?: 0f
            VisualizerScreen(a, b, c) { navController.popBackStack() }
        }
    }
}
