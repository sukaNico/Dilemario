package com.example.dilemario.ui

import android.util.Base64
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dilemario.ui.screens.*
import com.example.dilemario.data.UserPreferences
import kotlinx.coroutines.flow.firstOrNull
import org.json.JSONObject

@Composable
fun AppNavigation(navController: NavHostController) {
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }

    // Estado para token y userId
    var tokenReceived by remember { mutableStateOf<String?>(null) }
    var userId by remember { mutableStateOf<Int?>(null) }

    // Decodificar JWT y extraer id cuando token cambie
    LaunchedEffect(tokenReceived) {
        tokenReceived?.let {
            try {
                val parts = it.split(".")
                if (parts.size >= 2) {
                    val payload = parts[1]
                    val decoded = String(Base64.decode(payload, Base64.URL_SAFE))
                    val json = JSONObject(decoded)
                    userId = json.optInt("id", -1)
                    Log.d("AppNavigation", "User ID extraído del token: $userId")

                    // Establecer token en Retrofit
                    RetrofitClient.setToken(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                userId = -1
            }
        }
    }

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                onLoginSuccess = { newToken ->
                    tokenReceived = newToken // asignamos token recibido
                    navController.navigate("pager") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onGoToRegister = { navController.navigate("crearCuenta") }
            )
        }

        composable("pager") {
            userId?.let { id ->
                Log.d("AppNavigation", "Navegando a Pager con ID: $id")
                DilemaPagerScreen(navController, userId = id)
            }
        }

        composable("crearCuenta") {
            CreateAccountScreen(onRegister = {}, onBack = { navController.popBackStack() })
        }

        composable("config") { ConfigScreen(navController) }
        composable("adminDilemas") { AdminDilemasScreen(navController) }
        composable("editDilemma/{id}") { backStackEntry ->
            val dilemaId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            EditDilemmaScreen(navController, dilemaId)
        }
        composable("splash") { SplashScreen(navController) }
        composable("crearDilema") { CreateDilemmaScreen(navController) }
        composable("feed") { FeedScreen(navController) }
        composable("detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            DetailScreen(navController, id)
        }
        composable("profile") { ProfileScreen(navController) }
    }
}
