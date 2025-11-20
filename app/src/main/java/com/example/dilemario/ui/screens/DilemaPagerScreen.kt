package com.example.dilemario.ui.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dilemario.data.DilemaApi
import com.example.dilemario.data.UserPreferences
import com.example.dilemario.model.Dilema
import com.example.dilemario.ui.components.BottomNavigationBar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DilemaPagerScreen(navController: NavController, userId: Int) {

    val context = LocalContext.current
    val prefs: UserPreferences = remember { UserPreferences(context) }
    val apiService = RetrofitClient.api

    // Estado de dilemas y carga
    var dilemas by remember { mutableStateOf<List<DilemaApi>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Respuestas seleccionadas
    val respuestas = remember { mutableStateMapOf<Int, String?>() }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { dilemas.size + 1 } // +1 para la página de carga
    )

    val puedeHacerScroll = derivedStateOf {
        respuestas[pagerState.currentPage] != null || pagerState.currentPage == dilemas.size
    }

    val scope = rememberCoroutineScope()

    // -----------------------------
    // CARGA DE DILEMAS DESDE API CON TOKEN
    // -----------------------------
    LaunchedEffect(Unit) {
        // Obtener token guardado
        val userToken: String? = prefs.token.first()
        userToken?.let { RetrofitClient.setToken(it) }

        try {
            val response = apiService.getDilemasUnanswered(userId)
            if (response.success) {
                dilemas = response.data
                dilemas.forEach { d ->
                    Log.d("DilemaAPI", "Dilema recibido: ${d.titulo} (ID: ${d.id})")
                }
            } else {
                Log.d("DilemaAPI", "No se recibieron dilemas")
            }
        } catch (e: Exception) {
            Log.e("DilemaAPI", "Error al obtener dilemas", e)
        } finally {
            isLoading = false
        }
    }

    Scaffold(bottomBar = { BottomNavigationBar(navController) }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (isLoading) {
                LoadingMoreDilemas {}
            } else {
                VerticalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    userScrollEnabled = puedeHacerScroll.value
                ) { page ->
                    if (page < dilemas.size) {
                        val d = dilemas[page]
                        DilemaScreenSingle(
                            dilema = Dilema(
                                id = d.id,
                                titulo = d.titulo,
                                descripcion = d.descripcion,
                                opcionA = d.opcion_a,
                                opcionB = d.opcion_b,
                                votosOpcionA = 0,
                                votosOpcionB = 0,
                                categoria = d.categoria
                            ),
                            respuestaGuardada = respuestas[page],
                            onRespondido = { opcion -> respuestas[page] = opcion }
                        )
                    } else {
                        LoadingMoreDilemas {
                            scope.launch {
                                val nuevos = try {
                                    apiService.getDilemasUnanswered(userId).data.shuffled().take(5)
                                } catch (e: Exception) {
                                    emptyList()
                                }
                                dilemas = dilemas + nuevos
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingMoreDilemas(onLoad: () -> Unit) {
    LaunchedEffect(Unit) { onLoad() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF202020)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Cargando más dilemas...", color = Color.White, fontSize = 18.sp)
        }
    }
}
