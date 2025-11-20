package com.example.dilemario.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dilemario.data.LoginRequest
import com.example.dilemario.data.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit = {},
    onGoToRegister: () -> Unit = {}
) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }

    val backgroundColor = Color(0xFF11131B)
    val cardColor = Color(0xFF2A2D34)
    val textColor = Color(0xFFE6E6E6)
// 🔥 Cargar credenciales guardadas una sola vez al abrir la pantalla
    LaunchedEffect(Unit) {
        val savedEmail = prefs.loginEmail.firstOrNull()
        if (savedEmail != null) username = savedEmail

        val savedPass = prefs.loginPassword.firstOrNull()
        if (savedPass != null) password = savedPass
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Iniciar Sesión",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Campo correo
            TextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("Correo") },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = cardColor,
                    focusedContainerColor = cardColor,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = textColor,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(20.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Campo contraseña
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = cardColor,
                    focusedContainerColor = cardColor,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = textColor,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(20.dp)
            )

            Spacer(modifier = Modifier.height(35.dp))

            // Botón Login
            Button(
                onClick = {
                    val scope = CoroutineScope(Dispatchers.Main)

                    scope.launch {
                        try {
                            // Llamada API en hilo IO
                            val response = withContext(Dispatchers.IO) {
                                RetrofitClient.api.login(
                                    LoginRequest(
                                        email = username,
                                        password = password
                                    )
                                )
                            }

                            Log.d("API LOGIN", "Response completa: $response")

                            val loginData = response.data
                            if (loginData != null) {
                                val token = loginData.token
                                val user = loginData.user

                                // Guardar datos de usuario en DataStore
                                prefs.saveUserData(
                                    token = token,
                                    nombre = user.nombre,
                                    email = user.email,
                                    edad = user.edad_rango,
                                    pais = user.pais
                                )

                                // Guardar últimas credenciales usadas
                                prefs.saveLoginCredentials(username, password)

                                // ⚡ PASAR TOKEN RECIÉN OBTENIDO al navigation
                                onLoginSuccess(token)  // aquí se envía el token directamente

                            } else {
                                errorMessage = response.message ?: "Error desconocido del servidor"
                                Log.e("API LOGIN", "Login data nula: ${response.message}")
                            }

                        } catch (e: Exception) {
                            errorMessage = "Error: ${e.message}"
                            Log.e("API LOGIN", "ERROR LOGIN: $e")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2A2D34),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Entrar", fontSize = 18.sp)
            }


            Spacer(modifier = Modifier.height(18.dp))

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color.Red, fontSize = 14.sp)
            }

            TextButton(onClick = onGoToRegister) {
                Text("Crear cuenta nueva", color = Color.Gray)
            }
        }
    }
}
