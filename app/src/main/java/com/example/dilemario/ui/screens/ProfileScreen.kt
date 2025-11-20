package com.example.dilemario.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dilemario.R
import com.example.dilemario.ui.components.BottomNavigationBar

@Composable
fun ProfileScreen(navController: NavController) {

    val context = LocalContext.current
    val viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(context))

    val name by viewModel.name.collectAsState()
    val email by viewModel.email.collectAsState()
    val age by viewModel.age.collectAsState()
    val country by viewModel.country.collectAsState()

    Scaffold(
        containerColor = Color(0xFF202020),
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.profile_placeholder),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔥 DATOS REALES DEL USUARIO
            Text(
                text = name.ifEmpty { "Cargando..." },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = email,
                fontSize = 16.sp,
                color = Color.LightGray
            )

            Text(
                text = "Edad: $age",
                fontSize = 16.sp,
                color = Color.LightGray
            )

            Text(
                text = "País: $country",
                fontSize = 16.sp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF303030))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Estadísticas",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Dilemas contestados: 42", color = Color.LightGray)
                    Text(text = "Dilemas creados: 5", color = Color.LightGray)
                    Text(text = "Nivel: Intermedio", color = Color.LightGray)
                }
            }
        }
    }
}
